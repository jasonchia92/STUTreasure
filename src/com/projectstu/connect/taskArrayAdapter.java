package com.projectstu.connect;

import com.projectstu.stutreasure.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class taskArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String status;
	int[] resIds = new int[25];
	private final String[] values;


	SessionCookie sessionc;


	public taskArrayAdapter(Context context, String[] values, String stat) {
		super(context, R.layout.rowlayout, values);
		this.context = context;
		this.values = values;
		this.status = stat;

		//從伺服器取得完成紀錄,再做字串分割處理
		String[] bArray = status.split(",");

		//分割伺服器的資料以後,1為完成,0為未完成,寫入提示最尾端
		for (int i = 0; i < 25; i++) {
			if (bArray[i+2].contains("1")) {
				resIds[i] = R.drawable.correct;
			} else if (bArray[i+2].contains("0")) {
				resIds[i] = R.drawable.wrong;
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		textView.setText(values[position]);
		imageView.setImageResource(resIds[position]);
		

		// Change the icon for Windows and iPhone

		return rowView;
	}

} 