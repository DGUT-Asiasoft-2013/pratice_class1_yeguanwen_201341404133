package com.example.helloworld.fragments.inputcells;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.example.helloworld.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PictureInputCellFragment extends BaseInputCellFragment {

	final int REQUESTCODE_CAMERA = 1;
	final int REQUESTCODE_ALBUM  = 2;
	
	ImageView imageView;
	TextView labelText;
	TextView hintText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_inputcell_picture, container);

		imageView = (ImageView) view.findViewById(R.id.image);
		labelText = (TextView) view.findViewById(R.id.label);
		hintText = (TextView) view.findViewById(R.id.hint);

		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onImageViewClicked();
			}
		});

		return view;
	}

	void onImageViewClicked() {
		String[] items = { "拍照", "相册" };

		new AlertDialog.Builder(getActivity()).setTitle(labelText.getText())
		.setItems(items, new DialogInterface.OnClickListener() {        //不能与message同用
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							takePhoto();
							break;

						case 1:
							pickFromAlbum();
							break;

						default:
							break;
						}
					}
				})
		.setNegativeButton("取消", null)
		.show();
	}

	void takePhoto() {
		Intent itnt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  //拍照
		startActivityForResult(itnt, REQUESTCODE_CAMERA);  //开启活动并得到itnt结果
	}

	void pickFromAlbum() {
		Intent itnt = new Intent(Intent.ACTION_GET_CONTENT);  //从相册选取 
		itnt.setType("image/*");
		startActivityForResult(itnt, REQUESTCODE_ALBUM);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {   //取消拍照返回resultCode值
		if(resultCode == Activity.RESULT_CANCELED){
			return;
		}
		
		if(requestCode == REQUESTCODE_CAMERA){
			
			Bitmap bmp = (Bitmap) data.getExtras().get("data");
			imageView.setImageBitmap(bmp);
//			Log.d("camera capture", data.getExtras().keySet().toString());
//			Toast.makeText(getActivity(), data.getDataString(), Toast.LENGTH_LONG).show();
		}else if(requestCode == REQUESTCODE_ALBUM){
			Bitmap bmp;
			
			try {
				bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
				imageView.setImageBitmap(bmp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void setLabelText(String labelText) {
		this.labelText.setText(labelText);
	}

	@Override
	public void setHintText(String hintText) {
		this.hintText.setText(hintText);
	}
}
