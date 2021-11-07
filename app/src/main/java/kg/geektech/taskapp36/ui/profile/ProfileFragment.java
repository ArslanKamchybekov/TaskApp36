package kg.geektech.taskapp36.ui.profile;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import kg.geektech.taskapp36.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();

//        SharedPreferences preferences = getContext().getSharedPreferences("info", MODE_PRIVATE);
//        preferences.edit().putString("name", binding.editName.getText().toString());
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("name")
    }

    private void setListeners() {
        binding.imageProfile.setOnClickListener(view1 -> {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        binding.addSign.setVisibility(View.GONE);
        binding.textAddImage.setVisibility(View.GONE);
        galleryActivityResultLauncher.launch(intent);
        });
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri imageUri = data.getData();

                        binding.imageProfile.setImageURI(imageUri);
                    }
                    else {
                        Toast.makeText(requireContext(), "Cancelled...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
}