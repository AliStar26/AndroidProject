package com.example.androidproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidproject.API.ReceptionApiService;
import com.example.androidproject.API.ReceptionClient;
import com.example.androidproject.ClientDetailActivity;
import com.example.androidproject.Models.ClientDto;
import com.example.androidproject.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientsAdapter extends ArrayAdapter<ClientDto> {

    private OnClientVisitEndedListener listener;

    public ClientsAdapter(Context context, List<ClientDto> clients, OnClientVisitEndedListener listener) {
        super(context, 0, clients);
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClientDto client = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.client_item, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView phoneTextView = convertView.findViewById(R.id.phoneTextView);
        Button endVisitButton = convertView.findViewById(R.id.endVisitButton);

        nameTextView.setText(client.getName());
        phoneTextView.setText(client.getPhoneNumber());
        convertView.setOnClickListener(v -> {
            Log.d("ClientsAdapter", "Client clicked: " + client.getId());
            Intent intent = new Intent(getContext(), ClientDetailActivity.class);
            intent.putExtra("clientId", client.getKeyNumber());
            getContext().startActivity(intent);
        });
        endVisitButton.setOnClickListener(v -> {

            endVisit(client.getKeyNumber());
        });

        return convertView;
    }

    private void endVisit(int keyNumber) {
        ReceptionApiService apiService = ReceptionClient.getClient().create(ReceptionApiService.class);
        Call<Void> call = apiService.endVisit(keyNumber);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Визит завершен.", Toast.LENGTH_SHORT).show();
                    // Уведомляем об успешном завершении визита
                    if (listener != null) {
                        listener.onClientVisitEnded();
                    }
                } else {
                    Toast.makeText(getContext(), "Ошибка: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnClientVisitEndedListener {
        void onClientVisitEnded();
    }
}