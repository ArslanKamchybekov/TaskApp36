package kg.geektech.taskapp36.ui.task;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kg.geektech.taskapp36.R;
import kg.geektech.taskapp36.interfaces.OnItemClickListener;
import kg.geektech.taskapp36.models.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private ArrayList<Task> list;
    private OnItemClickListener onItemClickListener;

    public TaskAdapter() {
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
        if(position == -1){
            holder.itemView.setBackgroundColor(Color.parseColor("#567845"));
            holder.textTitle.setTextColor(Color.parseColor("#ffffff"));
        }else {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.textTitle.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(Task task) {
        list.add(0,task);
        notifyItemInserted(list.indexOf(task));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            itemView.setOnClickListener(view -> onItemClickListener.onClick(getAdapterPosition()));
            itemView.setOnLongClickListener(view -> {
                onItemClickListener.onLongClick(getAdapterPosition());
                return true;
            });
        }

        public void bind(Task task) {
            textTitle.setText(task.getText());
        }
    }
}
