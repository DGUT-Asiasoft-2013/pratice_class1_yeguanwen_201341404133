package com.example.helloworld.fragments;

import com.example.helloworld.R;
import com.example.helloworld.fragments.inputcells.SimpleTextInputCellFragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class PasswordRecoverStep2Fragment extends Fragment {

        String password;
//        SimpleTextInputCellFragment fragInputCellPin;
        SimpleTextInputCellFragment fragInputCellPassword;
        SimpleTextInputCellFragment fragInputCellPasswordRepeat;

        View view;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                if (view == null) {
                        view = inflater.inflate(R.layout.fragment_password_recover_step2, null);

//                        fragInputCellPin = (SimpleTextInputCellFragment) getFragmentManager()
//                                        .findFragmentById(R.id.input_pin);
                        fragInputCellPassword = (SimpleTextInputCellFragment) getFragmentManager()
                                        .findFragmentById(R.id.input_password);
                        fragInputCellPasswordRepeat = (SimpleTextInputCellFragment) getFragmentManager()
                                        .findFragmentById(R.id.input_password_repeat);
                        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                        onSubmit();
                                }
                        });
                }
                return view;
        }

        @Override
        public void onResume() {
                super.onResume();
//                fragInputCellPin.setLabelText("��֤��");
//                fragInputCellPin.setHintText("��������֤��");
                fragInputCellPassword.setLabelText("������");
                fragInputCellPassword.setHintText("������������");
                fragInputCellPassword.setEditText(true);
                fragInputCellPasswordRepeat.setLabelText("�ظ�����");
                fragInputCellPasswordRepeat.setHintText("���ظ�����������");
                fragInputCellPasswordRepeat.setEditText(true);
        }

        public static interface OnSubmitListener {
                void onSubmit();
        }

        OnSubmitListener onSubmitListener;

        public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
                this.onSubmitListener = onSubmitListener;
        }

        void onSubmit() {
                if (onSubmitListener != null) {
                        onSubmitListener.onSubmit();
                }
        }

        public String getText() {
                if (!fragInputCellPassword.getText().equals(fragInputCellPasswordRepeat.getText())) {
                        Toast.makeText(getActivity(), "�ظ����벻һ��", Toast.LENGTH_LONG).show();
                        return null;
                } else {
                        return fragInputCellPassword.getText();
                }
        }
}
