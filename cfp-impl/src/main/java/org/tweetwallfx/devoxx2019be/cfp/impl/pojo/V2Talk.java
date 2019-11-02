/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 TweetWallFX
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.tweetwallfx.devoxx2019be.cfp.impl.pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.tweetwallfx.util.ToString.createToString;
import static org.tweetwallfx.util.ToString.mapOf;
import static org.tweetwallfx.util.ToString.mapEntry;

public class V2Talk {

    private int id;
    private String title;
    private String description;
    private String videoURL;
    private List<V2Speaker> speakers = new ArrayList<V2Speaker>();
    private List<V2Tag> tags = new ArrayList<V2Tag>();
    private List<V2TimeSlot> timeSlots = new ArrayList<V2TimeSlot>();
    private int trackId;
    private String trackName;
    private String trackImageURL;
    private int sessionTypeId;
    private String sessionTypeName;
    private int sessionTypeDuration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public List<V2Speaker> getSpeakers() {
        return null == speakers
                ? Collections.emptyList()
                : Collections.unmodifiableList(speakers);
    }

    public void setSpeakers(List<V2Speaker> speakers) {
        this.speakers = speakers;
    }

    public List<V2Tag> getTags() {
        return null == tags
                ? Collections.emptyList()
                : Collections.unmodifiableList(tags);
    }

    public void setTags(List<V2Tag> tags) {
        this.tags = tags;
    }

    public List<V2TimeSlot> getTimeSlots() {
        return null == timeSlots
                ? Collections.emptyList()
                : Collections.unmodifiableList(timeSlots);
    }

    public void setTimeSlots(List<V2TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getTrackImageURL() {
        return trackImageURL;
    }

    public void setTrackImageURL(String trackImageURL) {
        this.trackImageURL = trackImageURL;
    }

    public int getSessionTypeId() {
        return sessionTypeId;
    }

    public void setSessionTypeId(int sessionTypeId) {
        this.sessionTypeId = sessionTypeId;
    }

    public String getSessionTypeName() {
        return sessionTypeName;
    }

    public void setSessionTypeName(String sessionTypeName) {
        this.sessionTypeName = sessionTypeName;
    }

    public int getSessionTypeDuration() {
        return sessionTypeDuration;
    }

    public void setSessionTypeDuration(int sessionTypeDuration) {
        this.sessionTypeDuration = sessionTypeDuration;
    }

    @Override
    public String toString() {
        return createToString(this, mapOf(
                mapEntry("id", getId()),
                mapEntry("title", getTitle()),
                mapEntry("description", getDescription()),
                mapEntry("videoURL", getVideoURL()),
                mapEntry("speakers", getSpeakers()),
                mapEntry("tags", getTags()),
                mapEntry("timeSlots", getTimeSlots()),
                mapEntry("trackId", getTrackId()),
                mapEntry("trackName", getTrackName()),
                mapEntry("trackImageURL", getTrackImageURL()),
                mapEntry("sessionTypeId", getSessionTypeId()),
                mapEntry("sessionTypeName", getSessionTypeName()),
                mapEntry("sessionTypeDuration", getSessionTypeDuration())
        ), super.toString());
    }
}
