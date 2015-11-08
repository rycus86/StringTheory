package hu.rycus.watchface.stringtheory.commons.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

import hu.rycus.watchface.stringtheory.commons.R;

public abstract class BaseWearableSubConfigurationActivity<T> extends Activity {

    public static final String EXTRA_PARENT = "sel$parent";
    public static final String EXTRA_SELECTED_INDEX = "sel$idx";
    public static final String EXTRA_RESULT = "sel$result";

    protected abstract List<T> getItems(final Configuration parent);

    protected abstract int getItemViewResource();

    protected abstract void onUpdateItemView(final View itemView, final T item);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_wear_default);

        final Configuration parent = (Configuration) getIntent().getSerializableExtra(EXTRA_PARENT);
        final int selectedIndex = getIntent().getIntExtra(EXTRA_SELECTED_INDEX, 0);

        final WearableListView listView = (WearableListView) findViewById(R.id.list_config);
        listView.setAdapter(new SelectionAdapter(getItems(parent)));
        listView.scrollToPosition(selectedIndex);
        listView.setClickListener(new WearableListView.ClickListener() {
            @Override
            public void onClick(final WearableListView.ViewHolder viewHolder) {
                final Serializable selection = (Serializable) viewHolder.itemView.getTag();
                final Intent data = new Intent();
                data.putExtra(EXTRA_PARENT, parent);
                data.putExtra(EXTRA_RESULT, selection);
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onTopEmptyRegionClick() { }
        });
    }

    private class SelectionAdapter extends WearableListView.Adapter {

        private final List<T> items;

        private SelectionAdapter(final List<T> items) {
            this.items = items;
        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                              final int viewType) {

            final LayoutInflater inflater =
                    LayoutInflater.from(BaseWearableSubConfigurationActivity.this);
            final View itemView = inflater.inflate(getItemViewResource(), null);
            return new ItemHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final WearableListView.ViewHolder holder,
                                     final int position) {

            final ItemHolder itemHolder = (ItemHolder) holder;
            final T item = items.get(position);
            itemHolder.update(item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private class ItemHolder extends WearableListView.ViewHolder {

        public ItemHolder(final View itemView) {
            super(itemView);
        }

        public void update(final T item) {
            onUpdateItemView(itemView, item);
            itemView.setTag(item);
        }

    }

}
