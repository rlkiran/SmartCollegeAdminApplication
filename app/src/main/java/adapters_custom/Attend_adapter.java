package adapters_custom;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.example.adminapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import cards.Attend_Item;


public  class Attend_adapter extends FirestoreRecyclerAdapter<Attend_Item, Attend_adapter.Attend_Holder> {
    private OnItemClickListener listener;
    public Attend_adapter(@NonNull FirestoreRecyclerOptions<Attend_Item> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Attend_adapter.Attend_Holder holder, int position, @NonNull Attend_Item model) {
        holder.cb.setChecked(model.getStatus().equals("present"));
        holder.cr_title.setText(model.getName());
        holder.cr_description.setText(model.getRoll());
    }


    @NonNull
    @Override
    public Attend_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attend_card_item,viewGroup,false);
        return new Attend_Holder(v);
    }

    class Attend_Holder extends RecyclerView.ViewHolder {

        TextView cr_title, cr_description;
        CheckBox cb;
        Attend_Holder(@NonNull View itemView)  {
            super(itemView);
            cb  = itemView.findViewById(R.id.cb_att);
            cr_title = itemView.findViewById(R.id.Circular_title);
            cr_description = itemView.findViewById(R.id.Circular_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Boolean checkcb = cb.isChecked();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position,checkcb);
                    }
                }
            });
        }

    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position,Boolean checkcb);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
