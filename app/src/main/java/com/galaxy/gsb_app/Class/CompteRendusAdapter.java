package com.galaxy.gsb_app.Class;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.galaxy.gsb_app.R;

import java.util.List;

public class CompteRendusAdapter extends ArrayAdapter<CompteRendus> {

    public CompteRendusAdapter(Context context, List<CompteRendus> cptRendus) {
        super(context, 0, cptRendus);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_list_compte_rendus,parent, false);
        }

        CompteRendusViewHolder viewHolder = (CompteRendusViewHolder)convertView.getTag();

        if(viewHolder == null){
            viewHolder = new CompteRendusViewHolder();
            viewHolder.NumCompteRendue = (TextView) convertView.findViewById(R.id.NumCompteRendue);
            viewHolder.DateCompteRendue = (TextView) convertView.findViewById(R.id.DateCompteRendue);
            viewHolder.Status = (ImageView) convertView.findViewById(R.id.Status);
            viewHolder.textViewNomPract = (TextView)convertView.findViewById(R.id.textViewNomPract);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<CompteRendus> cptRendus
        CompteRendus compteRendus = getItem(position);

        if (compteRendus != null) {
            viewHolder.NumCompteRendue.setText(String.valueOf(compteRendus.getId()));
            viewHolder.DateCompteRendue.setText(String.valueOf(compteRendus.getDate_rapport()));
            viewHolder.textViewNomPract.setText(String.valueOf(compteRendus.getPracticien_nom()));
            if (compteRendus.getSaisieDefinitive())
            {
                viewHolder.Status.setImageResource(R.drawable.ic_check_black_24dp);
            }
            else
            {
                viewHolder.Status.setImageResource(R.drawable.ic_clear_black_24dp);
            }
        }

        return convertView;
    }

    private class CompteRendusViewHolder{

        public TextView NumCompteRendue;
        public TextView DateCompteRendue;
        public ImageView Status;
        public TextView textViewNomPract;

        public TextView getNumCompteRendue() {
            return NumCompteRendue;
        }

        public void setNumCompteRendue(TextView numCompteRendue) {
            NumCompteRendue = numCompteRendue;
        }

        public TextView getDateCompteRendue() {
            return DateCompteRendue;
        }

        public void setDateCompteRendue(TextView dateCompteRendue) {
            DateCompteRendue = dateCompteRendue;
        }

        public ImageView getStatus() {
            return Status;
        }

        public void setStatus(ImageView status) {
            Status = status;
        }

        public TextView getTextViewNomPract() {
            return textViewNomPract;
        }

        public void setTextViewNomPract(TextView textViewNomPract) {
            this.textViewNomPract = textViewNomPract;
        }
    }
}