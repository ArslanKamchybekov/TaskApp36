package kg.geektech.taskapp36.ui.task;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kg.geektech.App;
import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.databinding.FragmentTaskBinding;
import kg.geektech.taskapp36.models.Task;


public class TaskFragment extends Fragment {

    private FragmentTaskBinding binding;
    private Task task;
    private ActivityResultLauncher<String> getImage;
    private String strUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTaskBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListeners();
        initViews();
    }

    private void initViews() {
        binding.progress.setVisibility(View.GONE);
        task = (Task) requireArguments().getSerializable("task");
        if (task != null) {
            binding.editText.setText(task.getText());
            Glide.with(requireContext()).load(task.getImgUri()).into(binding.imageTask);
        }
    }

    private void initListeners() {
        getImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                binding.imageTask.setImageURI(result);
                upload(result);
                binding.btnSave.setEnabled(false);
            }
        });
        binding.imageTask.setOnClickListener(v -> {
            getImage.launch("image/*");
        });
        binding.btnSave.setOnClickListener(view1 -> {
            if (!binding.editText.getText().toString().isEmpty() && binding.imageTask.getDrawable() != null) {
                save();
            } else {
                Toast.makeText(requireActivity(), "Choose an image or type the task...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void upload(Uri uri) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String userID = FirebaseAuth.getInstance().getUid();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child("IMG_" + userID + "_" + timeStamp + ".jpg");
        reference.putFile(uri).continueWithTask(task -> reference.getDownloadUrl()).addOnCompleteListener(task -> {
            strUri = task.getResult().toString();
            binding.btnSave.setEnabled(true);
        });
    }

    private void save() {
        binding.progress.setVisibility(View.VISIBLE);
        showProgress();

        String text = binding.editText.getText().toString().trim();

        if (task == null) {
            task = new Task(text, System.currentTimeMillis());
            if (strUri != null) {
                task.setImgUri(strUri);
            }
            App.getInstance().getDatabase().taskDao().insert(task);
            saveToFireStore(task);
        } else {
            if (!text.isEmpty()) {
                task.setText(text);
            }
            if (strUri != null) {
                task.setImgUri(strUri);
            }
            App.getInstance().getDatabase().taskDao().update(task);
            if (task.getDocId() != null) {
                updateInFirestore(task);
            } else {
                close();
            }
        }
    }

    private void showProgress() {
        binding.progress.setVisibility(View.VISIBLE);
        binding.btnSave.setVisibility(View.GONE);
    }

    private void hideProgress() {
        binding.progress.setVisibility(View.GONE);
        binding.btnSave.setVisibility(View.VISIBLE);
    }

    private void saveToFireStore(Task task) {
        FirebaseFirestore.getInstance()
                .collection("tasks")
                .add(task)
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()){
                        hideProgress();
                        close();
                    }
                });
    }

    private void updateInFirestore(Task task) {
        FirebaseFirestore.getInstance()
                .collection("tasks")
                .document(task.getDocId()).update("text", task.getText(), "imgUri", task.getImgUri())
                .addOnSuccessListener(unused -> {
                    Toast.makeText(requireActivity(), "Successfully updated", Toast.LENGTH_SHORT).show();
                    close();
                });
    }

    private void close() {
        binding.progress.setVisibility(View.GONE);
        binding.btnSave.setVisibility(View.VISIBLE);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigateUp();
    }
}