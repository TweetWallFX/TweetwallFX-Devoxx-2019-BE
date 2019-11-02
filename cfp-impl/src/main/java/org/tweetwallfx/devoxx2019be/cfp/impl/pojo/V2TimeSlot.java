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

public class V2TimeSlot {

    private int id;
    private String fromDate;
    private String toDate;
    private boolean overflow;
    private boolean reserved;
    private String remark;
    private int roomId;
    private String roomName;
    private int sessionTypeId;
    private String sessionTypeName;
    private int sessionTypeDuration;
    private boolean sessionTypeBreak;
    private String audienceLevel;
    private String langName;
    private String timezone;
    private String trackId;
    private String trackName;
    private String trackImageURL;
    private List<V2Speaker> speakers = new ArrayList<V2Speaker>();
    private List<V2Tag> tags = new ArrayList<V2Tag>();
    private int totalFavourites;
    private String talkDescription;
    private String talkTitle;
    private int talkId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public boolean isOverflow() {
        return overflow;
    }

    public void setOverflow(boolean overflow) {
        this.overflow = overflow;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
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

    public boolean isSessionTypeBreak() {
        return sessionTypeBreak;
    }

    public void setSessionTypeBreak(boolean sessionTypeBreak) {
        this.sessionTypeBreak = sessionTypeBreak;
    }

    public String getAudienceLevel() {
        return audienceLevel;
    }

    public void setAudienceLevel(String audienceLevel) {
        this.audienceLevel = audienceLevel;
    }

    public String getLangName() {
        return langName;
    }

    public void setLangName(String langName) {
        this.langName = langName;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
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

    public int getTotalFavourites() {
        return totalFavourites;
    }

    public void setTotalFavourites(int totalFavourites) {
        this.totalFavourites = totalFavourites;
    }

    public String getTalkDescription() {
        return talkDescription;
    }

    public void setTalkDescription(String talkDescription) {
        this.talkDescription = talkDescription;
    }

    public String getTalkTitle() {
        return talkTitle;
    }

    public void setTalkTitle(String talkTitle) {
        this.talkTitle = talkTitle;
    }

    public int getTalkId() {
        return talkId;
    }

    public void setTalkId(int talkId) {
        this.talkId = talkId;
    }

    @Override
    public String toString() {
        return createToString(this, mapOf(
                mapEntry("id", getId()),
                mapEntry("fromDate", getFromDate()),
                mapEntry("toDate", getToDate()),
                mapEntry("overflow", isOverflow()),
                mapEntry("reserved", isReserved()),
                mapEntry("remark", getRemark()),
                mapEntry("roomId", getRoomId()),
                mapEntry("roomName", getRoomName()),
                mapEntry("sessionTypeId", getSessionTypeId()),
                mapEntry("sessionTypeName", getSessionTypeName()),
                mapEntry("sessionTypeDuration", getSessionTypeDuration()),
                mapEntry("sessionTypeBreak", isSessionTypeBreak()),
                mapEntry("audienceLevel", getAudienceLevel()),
                mapEntry("langName", getLangName()),
                mapEntry("timezone", getTimezone()),
                mapEntry("trackId", getTrackId()),
                mapEntry("trackName", getTrackName()),
                mapEntry("trackImageURL", getTrackImageURL()),
                mapEntry("speakers", getSpeakers()),
                mapEntry("tags", getTags()),
                mapEntry("totalFavourites", getTotalFavourites()),
                mapEntry("talkDescription", getTalkDescription()),
                mapEntry("talkTitle", getTalkTitle()),
                mapEntry("talkId", getTalkId())
        ), super.toString());
    }
}
