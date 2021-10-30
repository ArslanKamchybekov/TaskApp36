package kg.geektech.taskapp36;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kg.geektech.taskapp36.databinding.FragmentTaskBinding;
import kg.geektech.taskapp36.models.Task;


public class TaskFragment extends Fragment {

    private FragmentTaskBinding binding;

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
        binding.btnSave.setOnClickListener(view1 -> {
            save();
        });
    }

    private void save() {
        String text = binding.editText.getText().toString().trim();
        Task task = new Task(text, System.currentTimeMillis());
        Bundle bundle = new Bundle();
        bundle.putSerializable("task", task);
        getParentFragmentManager().setFragmentResult("rk_task", bundle);
        close();
    }
    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigateUp();
    }
}