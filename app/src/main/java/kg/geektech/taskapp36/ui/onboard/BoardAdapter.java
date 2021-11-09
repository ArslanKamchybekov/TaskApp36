package kg.geektech.taskapp36.ui.onboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.databinding.ListBoardBinding;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;
import kg.geektech.taskapp36.prefs.Prefs;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {

    private String[] titles = new String[]{"Simple note-taking", "Manage your hobbies","with TaskApp!"};
    private int[] anims = new int[]{R.raw.first, R.raw.second, R.raw.third};
    private ListBoardBinding binding;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ListBoardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        
        public ViewHolder(@NonNull ListBoardBinding itemView) {
            super(itemView.getRoot());
            binding.btnStart.setOnClickListener(view -> {
                Prefs prefs = new Prefs(view.getContext());
                prefs.saveBoardState();
                Navigation.findNavController(itemView.getRoot()).navigate(R.id.navigation_home);
            });
        }

        public void bind(int position) {
            binding.textTitle.setText(titles[position]);
            binding.animationView.setAnimation(anims[position]);
            if (position == titles.length-1){
                binding.btnStart.setVisibility(View.VISIBLE);
            }else {
                binding.btnStart.setVisibility(View.GONE);
            }
        }
    }
}
