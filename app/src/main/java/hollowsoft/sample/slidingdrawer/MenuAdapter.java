package hollowsoft.sample.slidingdrawer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private final OnMenuListener listener;
    private final Menu[] menuArray;

    public MenuAdapter(final OnMenuListener listener, final Menu[] menuArray) {

        if (listener == null) {
            throw new IllegalArgumentException("The listener cannot be null.");
        }

        this.listener = listener;
        this.menuArray = menuArray;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewName;
        public ImageView imageViewIcon;

        public ViewHolder(final View view) {
            super(view);

            textViewName = (TextView) view.findViewById(R.id.menu_text_view_name);
            imageViewIcon = (ImageView) view.findViewById(R.id.menu_image_view_icon);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {

            final Menu menu = menuArray[getAdapterPosition()];

            listener.onMenuSelect(menu);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {

        final View view = LayoutInflater.from(viewGroup.getContext())
                                        .inflate(R.layout.menu_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        final Menu menu = menuArray[position];

        viewHolder.textViewName.setText(viewHolder.textViewName.getContext()
                                                               .getResources()
                                                               .getString(menu.getName()));

        viewHolder.imageViewIcon.setImageResource(menu.getIcon());
    }

    @Override
    public int getItemCount() {
        return menuArray.length;
    }
}
