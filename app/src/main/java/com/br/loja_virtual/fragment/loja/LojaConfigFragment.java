package com.br.loja_virtual.fragment.loja;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.br.loja_virtual.activity.loja.LojaConfigActivity;
import com.br.loja_virtual.activity.loja.LojaPagamentosActivity;
import com.br.loja_virtual.activity.usuario.MainActivityUsuario;
import com.br.loja_virtual.databinding.FragmentLojaConfigBinding;
import com.br.loja_virtual.helper.FirebaseHelper;

public class LojaConfigFragment extends Fragment {

    private FragmentLojaConfigBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLojaConfigBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configClicks();
    }

    private void configClicks() {
        binding.btnConfigLoja.setOnClickListener(v -> {
            startActivity(LojaConfigActivity.class);
        });

        binding.btnPagamentos.setOnClickListener(v -> {
            startActivity(LojaPagamentosActivity.class);
        });

        binding.btnDeslogar.setOnClickListener(v -> {
            FirebaseHelper.getAuth().signOut();
            requireActivity().finish();
            startActivity(MainActivityUsuario.class);
        });
    }

    private void startActivity(Class<?> clazz) {
        startActivity(new Intent(requireContext(), clazz));
    }

}