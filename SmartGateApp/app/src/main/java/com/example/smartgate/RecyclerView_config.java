package com.example.smartgate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class RecyclerView_config implements Serializable {

    private Context mContext;
    private AuthPeopleAdapter mAuthPersonAdapter;


    public void setConfig(RecyclerView recyclerView, Context context, List<AuthorizedPerson> authorizedPeople , List<String> keys)
    {
        mContext=context;
        mAuthPersonAdapter = new AuthPeopleAdapter(authorizedPeople,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mAuthPersonAdapter);
    }


    class AuthPersonItemView extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mFirstName;
        private TextView mLastName;
        private TextView mIDNumber;
        private String key;
        private ImageView personIm;




        public AuthPersonItemView(ViewGroup parent)
        {
            super(LayoutInflater.from(mContext).inflate(R.layout.authpeople_list_item,parent,false));


            mFirstName =(TextView)itemView.findViewById(R.id.firstName);
            mLastName =(TextView)itemView.findViewById(R.id.lastName);
            mIDNumber =(TextView)itemView.findViewById(R.id.id_number);
            personIm = (ImageView)itemView.findViewById(R.id.imageView_authPeopleList);

        }

        public void bind(AuthorizedPerson authorizedPerson , String IDNumber)
        {
            mFirstName.setText(authorizedPerson.getFirstName());
            mLastName.setText(authorizedPerson.getLastName());
            mIDNumber.setText(IDNumber);
            Picasso.get().load(authorizedPerson.getUrlImage()).into(personIm);

        }


        @Override
        public void onClick(View v) {

            if ( v== itemView){
                Intent intent = new Intent(mContext, AuthorizedPersonDetailsActivity.class);

                intent.putExtra("First Name", mFirstName.getText().toString());
                intent.putExtra("Last Name", mLastName.getText().toString());
                intent.putExtra("ID Number", mIDNumber.getText().toString());

                mContext.startActivity(intent);

            }
        }
    }

    class AuthPeopleAdapter extends  RecyclerView.Adapter<AuthPersonItemView>
    {
        private List<AuthorizedPerson> mAuthPeopleList;
        private List<String> IDNumbers;

        public AuthPeopleAdapter(List<AuthorizedPerson> mAuthPeopleList, List<String> IDNumbers)
        {
            this.mAuthPeopleList = mAuthPeopleList;
            this.IDNumbers = IDNumbers;
        }

        @NonNull
        @Override
        public AuthPersonItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            return new AuthPersonItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull AuthPersonItemView holder, int position)
        {
            holder.bind(mAuthPeopleList.get(position), IDNumbers.get(position));

        }

        @Override
        public int getItemCount()
        {
            return mAuthPeopleList.size();
        }
    }


}
