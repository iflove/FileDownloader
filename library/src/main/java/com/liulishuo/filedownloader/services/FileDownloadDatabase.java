/*
 * Copyright (c) 2015 LingoChamp Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liulishuo.filedownloader.services;


import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.util.List;

/**
 * The filedownloader database, what is used for storing the {@link FileDownloadModel}.
 * <p/>
 * The data stored in the database is only used for task resumes from the breakpoint.
 * <p>
 * The task of the data stored in the database must be a task that has not finished downloading yet,
 * and if the task has finished downloading, its data will be {@link #remove(int)} from the database,
 * since that data is no longer available for resumption of its task pass.
 *
 * @see DefaultDatabaseImpl
 * @see FileDownloadUtils#isBreakpointAvailable(int, FileDownloadModel)
 */
public interface FileDownloadDatabase {

    /**
     * Find the model which identify is {@code id}.
     *
     * @param id the download id.
     */
    FileDownloadModel find(final int id);

    /**
     * Find the connection model which download identify is {@code id}
     *
     * @param id the download id.
     */
    List<ConnectionModel> findConnectionModel(int id);

    /**
     * Delete all connection model store on database through the download id.
     *
     * @param id the download id.
     */
    void removeConnections(int id);

    /**
     * Insert the {@code model} to connection table.
     *
     * @param model the connection model.
     */
    void insertConnectionModel(ConnectionModel model);

    /**
     * Update the currentOffset with {@code currentOffset} which id is {@code id}, index is
     * {@code index}
     *
     * @param id            the download id.
     * @param index         the connection index.
     * @param currentOffset the current offset.
     */
    void updateConnectionModel(int id, int index, long currentOffset);

    /**
     * Update the count of connection.
     *
     * @param count the connection count.
     */
    void updateConnectionCount(FileDownloadModel model, int count);

    /**
     * Insert the model to the database.
     *
     * @param downloadModel the download model.
     */
    void insert(final FileDownloadModel downloadModel);

    /**
     * Update the data compare to the {@code downloadModel}
     *
     * @param downloadModel the download model.
     */
    void update(final FileDownloadModel downloadModel);

    /**
     * Update the batch of datum compare to the {@code downloadModelList}
     *
     * @param downloadModelList the list of model.
     */
    void update(final List<FileDownloadModel> downloadModelList);

    /**
     * Remove the model which identify is {@code id}.
     *
     * @param id the download id.
     * @return {@code true} if succeed to remove model from the database.
     */
    boolean remove(final int id);

    /**
     * Clear all models in this database.
     */
    void clear();


    /**
     * Update the etag when the old one is overdue.
     *
     * @param newEtag the new etag.
     */
    void updateOldEtagOverdue(FileDownloadModel model, String newEtag);

    /**
     * Update the data because of the download status alternative to {@link FileDownloadStatus#connected}.
     *
     * @param model    the data in the model will be updated.
     * @param total    the new total bytes.
     * @param etag     the new etag.
     * @param fileName the new file name.
     */
    void updateConnected(final FileDownloadModel model, final long total, final String etag,
                         final String fileName);

    /**
     * Sync progress form buffer {@code model}.
     *
     * @param model the data in the model will be updated.
     */
    void syncProgressFromCache(final FileDownloadModel model);

    /**
     * Update the data because of the download status alternative to {@link FileDownloadStatus#error}.
     *
     * @param model     the data in the model will be updated.
     * @param throwable the new exception.
     * @param sofar     the new so far bytes.
     */
    void updateError(final FileDownloadModel model, final Throwable throwable, final long sofar);

    /**
     * Update the data because of the download status alternative to {@link FileDownloadStatus#retry}.
     *
     * @param model     the data in the model will be updated.
     * @param throwable the new exception.
     */
    void updateRetry(final FileDownloadModel model, final Throwable throwable);

    /**
     * Update the data because of the download status alternative to {@link FileDownloadStatus#completed}.
     * The latest version will remove model from DB.
     *
     * @param model the data in the model will be updated.
     * @param total the new total bytes.
     */
    void updateComplete(final FileDownloadModel model, final long total);

    /**
     * Update the data because of the download status alternative to {@link FileDownloadStatus#paused}.
     *
     * @param model the data in the model will be updated.
     * @param sofar the new so far bytes.
     */
    void updatePause(final FileDownloadModel model, final long sofar);

    /**
     * Update the data because of the download status alternative to {@link FileDownloadStatus#pending}.
     *
     * @param model the data in the model will be updated.
     */
    void updatePending(final FileDownloadModel model);
}
