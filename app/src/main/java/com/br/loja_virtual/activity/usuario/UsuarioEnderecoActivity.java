package com.br.loja_virtual.activity.usuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.br.loja_virtual.R;
import com.br.loja_virtual.adapter.EnderecoAdapter;
import com.br.loja_virtual.databinding.ActivityUsuarioEnderecoBinding;
import com.br.loja_virtual.databinding.DialogDeleteBinding;
import com.br.loja_virtual.helper.FirebaseHelper;
import com.br.loja_virtual.model.Endereco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsuarioEnderecoActivity extends AppCompatActivity implements EnderecoAdapter.OnClickListener {

    private ActivityUsuarioEnderecoBinding binding;

    private EnderecoAdapter enderecoAdapter;

    private final List<Endereco> enderecoList = new ArrayList<>();

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsuarioEnderecoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iniciaComponentes();

        configClicks();

        configRv();
    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperaEndereco();
    }

    private void configRv() {
        binding.rvEnderecos.setLayoutManager(new LinearLayoutManager(this));
        binding.rvEnderecos.setHasFixedSize(true);
        enderecoAdapter = new EnderecoAdapter(enderecoList, this, this);
        binding.rvEnderecos.setAdapter(enderecoAdapter);

        binding.rvEnderecos.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {

            }

            @Override
            public void onSwipedRight(int position) {
                showDialogDelete(enderecoList.get(position));
            }
        });
    }

    private void showDialogDelete(Endereco endereco) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this, R.style.CustomAlertDialog2);

        DialogDeleteBinding deleteBinding = DialogDeleteBinding
                .inflate(LayoutInflater.from(this));

        deleteBinding.btnFechar.setOnClickListener(v -> {
            dialog.dismiss();
            enderecoAdapter.notifyDataSetChanged();
        });

        deleteBinding.textTitulo.setText("Deseja remover este endere??o ?");

        deleteBinding.btnSim.setOnClickListener(v -> {
            enderecoList.remove(endereco);

            if(enderecoList.isEmpty()){
                binding.textInfo.setText("Nenhum endere??o cadastrado.");
            }else {
                binding.textInfo.setText("");
            }

            endereco.delete();

            enderecoAdapter.notifyDataSetChanged();

            dialog.dismiss();
        });

        builder.setView(deleteBinding.getRoot());

        dialog = builder.create();
        dialog.show();
    }

    private void recuperaEndereco() {
        DatabaseReference enderecoRef = FirebaseHelper.getDatabaseReference()
                .child("enderecos")
                .child(FirebaseHelper.getIdFirebase());
        enderecoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    enderecoList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Endereco endereco = ds.getValue(Endereco.class);
                        enderecoList.add(endereco);
                    }
                    binding.textInfo.setText("");
                } else {
                    binding.textInfo.setText("Nenhum endere??o cadastrado.");
                }

                binding.progressBar.setVisibility(View.GONE);
                Collections.reverse(enderecoList);
                enderecoAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configClicks() {
        binding.include.btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, UsuarioFormEnderecoActivity.class))
        );
    }

    private void iniciaComponentes() {
        binding.include.textTitulo.setText("Meus endere??os");
        binding.include.include.ibVoltar.setOnClickListener(v -> finish());
    }

    @Override
    public void onClick(Endereco endereco) {
        Intent intent = new Intent(this, UsuarioFormEnderecoActivity.class);
        intent.putExtra("enderecoSelecionado", endereco);
        startActivity(intent);
    }
}