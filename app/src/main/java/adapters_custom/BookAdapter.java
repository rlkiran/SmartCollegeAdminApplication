package adapters_custom;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adminapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import cards.Book;

public class BookAdapter extends FirestoreRecyclerAdapter<Book, BookAdapter.BookHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BookAdapter(@NonNull FirestoreRecyclerOptions<Book> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BookHolder holder, int position, @NonNull Book model) {
        int count = model.getCount();
        String author = "Authors : " + model.getAuthor();
        holder.tv_title.setText(model.getTitle());
        holder.tv_author.setText(author);
        if(count>=0) {
            holder.tv_count.setText(String.valueOf(model.getCount()));
        }else {
            holder.tv_count.setText(model.getCount());
        }

    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_card,viewGroup,false);

        return new BookHolder(v);
    }

    class BookHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_author, tv_count;

        BookHolder(@NonNull final View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.book_title);
            tv_author = itemView.findViewById(R.id.book_author);
            tv_count = itemView.findViewById(R.id.book_count);

        }
    }


}
