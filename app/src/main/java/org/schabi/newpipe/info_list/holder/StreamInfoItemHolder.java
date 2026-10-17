package org.schabi.newpipe.info_list.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.schabi.newpipe.R;
import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.extractor.stream.StreamType;
import org.schabi.newpipe.info_list.InfoItemBuilder;
import org.schabi.newpipe.local.history.HistoryRecordManager;
import org.schabi.newpipe.util.ContentFilterEngine;
import org.schabi.newpipe.util.Localization;

/*
 * Created by Christian Schabesberger on 01.08.16.
 * <p>
 * Copyright (C) Christian Schabesberger 2016 <chris.schabesberger@mailbox.org>
 * StreamInfoItemHolder.java is part of NewPipe.
 * </p>
 */

public class StreamInfoItemHolder extends StreamMiniInfoItemHolder {
    public final TextView itemAdditionalDetails;

    public StreamInfoItemHolder(final InfoItemBuilder infoItemBuilder, final ViewGroup parent) {
        this(infoItemBuilder, R.layout.list_stream_item, parent);
    }

    public StreamInfoItemHolder(final InfoItemBuilder infoItemBuilder, final int layoutId,
                                final ViewGroup parent) {
        super(infoItemBuilder, layoutId, parent);
        itemAdditionalDetails = itemView.findViewById(R.id.itemAdditionalDetails);
    }

    @Override
    public void updateFromItem(final InfoItem infoItem,
                               final HistoryRecordManager historyRecordManager) {
        super.updateFromItem(infoItem, historyRecordManager);

        if (!(infoItem instanceof StreamInfoItem)) {
            return;
        }
        final StreamInfoItem item = (StreamInfoItem) infoItem;

        String title = item.getName();
        String uploader = item.getUploaderName();

        ContentFilterEngine filter = ContentFilterEngine.Companion.getInstance(getContext());
        if (filter.isRussianContent(title, uploader)) {
            itemView.setVisibility(View.GONE);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            return;
        }

        itemAdditionalDetails.setText(getStreamInfoDetailLine(item));
    }

    private String getStreamInfoDetailLine(final StreamInfoItem infoItem) {
        String viewsAndDate = "";
        if (infoItem.getViewCount() >= 0) {
            if (infoItem.getStreamType().equals(StreamType.AUDIO_LIVE_STREAM)) {
                viewsAndDate = Localization
                        .listeningCount(itemBuilder.getContext(), infoItem.getViewCount());
            } else if (infoItem.getStreamType().equals(StreamType.LIVE_STREAM)) {
                viewsAndDate = Localization
                        .shortWatchingCount(itemBuilder.getContext(), infoItem.getViewCount());
            } else {
                viewsAndDate = Localization
                        .shortViewCount(itemBuilder.getContext(), infoItem.getViewCount());
            }
        }

        final String uploadDate = Localization.relativeTimeOrTextual(itemBuilder.getContext(),
                infoItem.getUploadDate(),
                infoItem.getTextualUploadDate());
        if (!TextUtils.isEmpty(uploadDate)) {
            if (viewsAndDate.isEmpty()) {
                return uploadDate;
            }

            return Localization.concatenateStrings(viewsAndDate, uploadDate);
        }

        return viewsAndDate;
    }
}
