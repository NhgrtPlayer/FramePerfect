package com.corp.detective.frameperfect.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.corp.detective.frameperfect.R;
import com.corp.detective.database.entities.Character;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CharacterDetailsFragment extends Fragment {
    @BindView(R.id.character_details_name)
    TextView txtName;

    @BindView(R.id.character_details_description)
    TextView txtDescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_character_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, getView());

        showCharacterDetails();
    }

    private void showCharacterDetails() {
        // TODO: Make character details
        Bundle data = this.getArguments();
        int characterId = data.getInt("id", -1);

        if(characterId != -1){
            Character character = Character.getCharacterById(characterId);

            txtName.setText(character.getName());
            txtDescription.setText(character.getDescription());
        }
    }
}
