package kg.geektech.taskapp36.ui.task;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import kg.geektech.App;
import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.databinding.FragmentTaskBinding;
import kg.geektech.taskapp36.models.Task;


public class TaskFragment extends Fragment {

    private FragmentTaskBinding binding;
    private Task task;
    private ArrayList<Task> list = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        task = (Task) requireArguments().getSerializable("task1");
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
        if (task != null) {
            binding.editText.setText(task.getText());
        }
    }

    private void initListeners() {
        binding.btnSave.setOnClickListener(view1 -> {
            save();
        });
    }

    private void save() {
        String text = binding.editText.getText().toString().trim();
        Task task = new Task(text, System.currentTimeMillis());
        Bundle bundle = new Bundle();
        if (this.task != null) {
            App.getInstance().getDatabase().taskDao().update(task);
            bundle.putSerializable("updated", task);
        } else {
            App.getInstance().getDatabase().taskDao().insert(task);
            bundle.putSerializable("task", task);
        }
        getParentFragmentManager().setFragmentResult("rk_task", bundle);
        close();
    }

    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigateUp();
    }
}