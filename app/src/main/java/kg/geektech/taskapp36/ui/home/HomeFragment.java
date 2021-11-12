package kg.geektech.taskapp36.ui.home;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import kg.geektech.App;
import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.databinding.FragmentHomeBinding;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;
import kg.geektech.taskapp36.models.Task;
import kg.geektech.taskapp36.ui.task.TaskAdapter;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TaskAdapter adapter;
    private Task task;
    private int pos;
    private boolean checker;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        adapter = new TaskAdapter();
        adapter.addItems(App.getInstance().getDatabase().taskDao().getAll());
        adapter.lastToTop();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                pos = position;
                task = adapter.getItem(position);
                openFragment(task);
            }

            @Override
            public void onLongClick(int position) {
                task = adapter.getItem(position);
                App.getInstance().getDatabase().taskDao().delete(task);
                adapter.removeItem(position);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();
        binding.fab.setOnClickListener(view1 -> {
            pos = -1;
            openFragment(null);
        });
        getParentFragmentManager().setFragmentResultListener("rk_task", getViewLifecycleOwner(), (requestKey, result) -> {
            task = (Task) result.getSerializable("task");
            if (result.getSerializable("updated") != null) {
                task = (Task) result.getSerializable("updated");
                adapter.editItem(task, pos);
            } else {
                adapter.addItem(task);
            }
        });
    }

    private void initList() {
        binding.recyclerView.setAdapter(adapter);
    }

    private void openFragment(Task task) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("task1", task);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.taskFragment, bundle);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.fromAToZ) {
            if (!checker) {
                adapter.fromAToZ();
                checker = true;
            }
            return true;
        } else if (item.getItemId() == R.id.defaultList) {
            adapter.clearList();
            adapter.addItems(App.getInstance().getDatabase().taskDao().getAll());
            adapter.lastToTop();
            checker = false;
        }
        return super.onOptionsItemSelected(item);
    }
}