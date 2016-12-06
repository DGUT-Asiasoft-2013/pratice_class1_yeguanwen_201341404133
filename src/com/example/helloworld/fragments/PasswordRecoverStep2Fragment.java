package com.example.helloworld.fragments;

import com.example.helloworld.R;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PasswordRecoverStep2Fragment extends Fragment {
        
        SimpleTextInputCellFragment fragInputCellPin;
        SimpleTextInputCellFragment fragInputCellPassword;
        SimpleTextInputCellFragment fragInputCellPasswordRepeat;

        View view;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                if (view == null) {
                        view = inflater.inflate(R.layout.fragment_password_recover_step2, null);
                        
                        fragInputCellPin = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_pin);
                        fragInputCellPassword = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password);
                        fragInputCellPasswordRepeat = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password_repeat);
                        
                }
                return view;
        }
        @Override
        public void onResume() {
                super.onResume();
                fragInputCellPin.setLabelText("��֤��");
                fragInputCellPin.setHintText("��������֤��");
                fragInputCellPassword.setLabelText("������");
                fragInputCellPassword.setHintText("������������");
                fragInputCellPassword.setEditText(true);
                fragInputCellPasswordRepeat.setLabelText("�ظ�����");
                fragInputCellPasswordRepeat.setHintText("���ظ�����������");
                fragInputCellPasswordRepeat.setEditText(true);
        }
}
