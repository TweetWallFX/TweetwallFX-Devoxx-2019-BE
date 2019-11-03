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
package org.tweetwallfx.devoxx2019be.cfp.impl;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.core.GenericType;
import org.tweetwallfx.devoxx.api.cfp.client.CFPClient;
import org.tweetwallfx.devoxx.api.cfp.client.Event;
import org.tweetwallfx.devoxx.api.cfp.client.Events;
import org.tweetwallfx.devoxx.api.cfp.client.ProposalTypes;
import org.tweetwallfx.devoxx.api.cfp.client.Rooms;
import org.tweetwallfx.devoxx.api.cfp.client.Schedule;
import org.tweetwallfx.devoxx.api.cfp.client.Schedules;
import org.tweetwallfx.devoxx.api.cfp.client.Speaker;
import org.tweetwallfx.devoxx.api.cfp.client.Talk;
import org.tweetwallfx.devoxx.api.cfp.client.Tracks;
import org.tweetwallfx.devoxx.api.cfp.client.VotingResults;
import static org.tweetwallfx.devoxx.api.cfp.client.impl.RestCallHelper.readOptionalFrom;
import org.tweetwallfx.config.Configuration;
import org.tweetwallfx.devoxx.api.cfp.client.Break;
import org.tweetwallfx.devoxx.api.cfp.client.Link;
import org.tweetwallfx.devoxx.api.cfp.client.ProposalType;
import org.tweetwallfx.devoxx.api.cfp.client.Room;
import org.tweetwallfx.devoxx.api.cfp.client.ScheduleSlot;
import org.tweetwallfx.devoxx.api.cfp.client.SpeakerReference;
import org.tweetwallfx.devoxx.api.cfp.client.Tag;
import org.tweetwallfx.devoxx.api.cfp.client.Track;
import org.tweetwallfx.devoxx2019be.cfp.impl.pojo.V2Event;
import org.tweetwallfx.devoxx2019be.cfp.impl.pojo.V2Link;
import org.tweetwallfx.devoxx2019be.cfp.impl.pojo.V2Proposal;
import org.tweetwallfx.devoxx2019be.cfp.impl.pojo.V2Room;
import org.tweetwallfx.devoxx2019be.cfp.impl.pojo.V2Schedules;
import org.tweetwallfx.devoxx2019be.cfp.impl.pojo.V2SessionType;
import org.tweetwallfx.devoxx2019be.cfp.impl.pojo.V2Speaker;
import org.tweetwallfx.devoxx2019be.cfp.impl.pojo.V2Tag;
import org.tweetwallfx.devoxx2019be.cfp.impl.pojo.V2Talk;
import org.tweetwallfx.devoxx2019be.cfp.impl.pojo.V2TimeSlot;
import org.tweetwallfx.devoxx2019be.cfp.impl.pojo.V2Track;

public class Devoxx2019BECFPClient implements CFPClient {

    private static final GenericType<List<V2Room>> GT_ROOM = new GenericType<List<V2Room>>() {
    };
    private static final GenericType<List<V2SessionType>> GT_SESSION_TYPE = new GenericType<List<V2SessionType>>() {
    };
    private static final GenericType<List<V2Speaker>> GT_SPEAKER = new GenericType<List<V2Speaker>>() {
    };
    private static final GenericType<List<V2TimeSlot>> GT_TIME_SLOT = new GenericType<List<V2TimeSlot>>() {
    };
    private static final GenericType<List<V2Track>> GT_TRACK = new GenericType<List<V2Track>>() {
    };
    private static final DateTimeFormatter HOUR_MINUTE = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .toFormatter();
    private final String eventUri;
    private final String votingResultsUri;

    public Devoxx2019BECFPClient() {
        final CFPClientSettings cfpClientSettings = Configuration.getInstance().getConfigTyped(
                CFPClientSettings.CONFIG_KEY,
                CFPClientSettings.class);

        eventUri = cfpClientSettings.getEventUri();
        votingResultsUri = null; // disabled as a first shot to get the schedule shown
//        votingResultsUri = cfpClientSettings.getVotingResultsUri();
    }

    @Override
    public boolean canGetVotingResults() {
        return null != votingResultsUri;
    }

    @Override
    public Optional<Event> getEvent() {
        return readOptionalFrom(eventUri + "/event", V2Event.class)
                .map(this::convertToEvent);
    }

    @Override
    public Optional<Events> getEvents() {
        // There's no such thing as Events in this version of the API
        throw new UnsupportedOperationException(
                "Devoxx 2019 BE CFP API does not support multiple Events at one CFP location.");
    }

    @Override
    public Optional<ProposalTypes> getProposalTypes() {
        return readOptionalFrom(eventUri + "/session-types/", GT_SESSION_TYPE)
                .map(this::convertToProposalTypes);
    }

    @Override
    public Optional<Rooms> getRooms() {
        return readOptionalFrom(eventUri + "/rooms/", GT_ROOM)
                .map(this::convertToRooms);
    }

    @Override
    public Optional<Schedule> getSchedule(final String day) {
        return readOptionalFrom(eventUri + "/schedules/" + day, GT_TIME_SLOT)
                .map(this::convertToSchedule);
    }

    @Override
    public Optional<Schedule> getSchedule(final String day, final String room) {
        return readOptionalFrom(eventUri + "/schedules/" + day + '/' + room, GT_TIME_SLOT)
                .map(this::convertToSchedule);
    }

    @Override
    public Optional<Schedules> getSchedules() {
        return readOptionalFrom(eventUri + "/schedules/", V2Schedules.class)
                .map(this::convertToSchedules);
    }

    @Override
    public Optional<Speaker> getSpeaker(final String speakerId) {
        return readOptionalFrom(eventUri + "/speakers/" + speakerId, V2Speaker.class)
                .map(o -> convertToSpeaker(o, true));
    }

    @Override
    public List<Speaker> getSpeakers() {
        return readOptionalFrom(eventUri + "/speakers/", GT_SPEAKER)
                .map(list -> list.stream()
                .map(o -> convertToSpeaker(o, false))
                .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    public Optional<Talk> getTalk(final String talkId) {
        return readOptionalFrom(eventUri + "/talks/" + talkId, V2Talk.class)
                .map(this::convertToTalk);
    }

    @Override
    public Optional<Tracks> getTracks() {
        return readOptionalFrom(eventUri + "/tracks", GT_TRACK)
                .map(list -> {
                    final Tracks result = new Tracks();

                    result.setTracks(list.stream()
                            .map(this::convertToTrack)
                            .collect(Collectors.toList()));

                    return result;
                });
    }

    @Override
    public Optional<VotingResults> getVotingResultsDaily(final String day) {
        return Optional.ofNullable(votingResultsUri)
                .flatMap(uri -> readOptionalFrom(uri, Collections.singletonMap("day", day), VotingResults.class));
    }

    @Override
    public Optional<VotingResults> getVotingResultsOverall() {
        return Optional.ofNullable(votingResultsUri)
                .flatMap(uri -> readOptionalFrom(uri, VotingResults.class));
    }

    private Event convertToEvent(final V2Event input) {
        final Event result = new Event();

        result.setDays(Instant.parse(input.getFromDate()).atZone(ZoneId.systemDefault()).toLocalDate()
                .datesUntil(Instant.parse(input.getToDate()).atZone(ZoneId.systemDefault()).toLocalDate().plus(1L, ChronoUnit.DAYS))
                .map(LocalDate::getDayOfWeek)
                .map(DayOfWeek::name)
                .map(String::toLowerCase)
                .collect(Collectors.toList()));
        result.setEventCode(input.getSlug());
//        result.setLabel(input.get);
//        result.setLink(input.get);
//        result.setLinks(input.get);
//        result.setLocale(input.get);
//        result.setLocalisation(input.get);
        result.setProposalTypesId(input.getSessionTypes().stream()
                .mapToInt(V2SessionType::getId)
                .mapToObj(Integer::toString)
                .collect(Collectors.toList()));

        return result;
    }

    private ProposalType convertToProposalType(final V2SessionType input) {
        final ProposalType result = new ProposalType();

        result.setDescription(input.getDescription());
        result.setDuration(input.getDuration());
        result.setId(Integer.toString(input.getId()));
        result.setLabel(input.getName());

        return result;
    }

    private ProposalTypes convertToProposalTypes(final List<V2SessionType> list) {
        final ProposalTypes result = new ProposalTypes();

//        result.setContent(null);
        result.setProposalTypes(list.stream()
                .map(this::convertToProposalType)
                .collect(Collectors.toList()));

        return result;
    }

    private Room convertToRoom(final V2Room input) {
        final Room result = new Room();

        result.setCapacity(-1);
        result.setId(Integer.toString(input.getId()));
        result.setName(input.getName());
        result.setRecorded("unknown");
        result.setSetup(null);

        return result;
    }

    private Rooms convertToRooms(final List<V2Room> input) {
        final Rooms result = new Rooms();

//        result.setContent(input.);
        result.setRooms(input.stream()
                .map(this::convertToRoom)
                .collect(Collectors.toList()));

        return result;
    }

    private Schedules convertToSchedules(final V2Schedules input) {
        final Schedules result = new Schedules() {

            @Override
            public Stream<Schedule> getSchedules() {
                return getLinkStream(Link.Type.SCHEDULE)
                        .map(Link::getHref)
                        .map(urlString -> readOptionalFrom(urlString, GT_TIME_SLOT))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(Devoxx2019BECFPClient.this::convertToSchedule);
            }
        };

        result.setLinks(input.getLinks().stream()
                .map(this::createScheduleLink)
                .collect(Collectors.toList()));

        return result;
    }

    private ScheduleSlot convertToScheduleSlot(final V2TimeSlot input) {
        final ScheduleSlot result = new ScheduleSlot();

        result.setDay(Instant.parse(input.getFromDate()).atZone(ZoneId.systemDefault()).getDayOfWeek().name().toLowerCase(Locale.ENGLISH));
        result.setFromTime(Instant.parse(input.getFromDate()).atZone(ZoneId.systemDefault()).format(HOUR_MINUTE));
        result.setFromTimeMillis(Instant.parse(input.getFromDate()).toEpochMilli());
//        result.setNotAllocated(input.);
        result.setRoomCapacity(-1);
        result.setRoomId(Integer.toString(input.getRoomId()));
        result.setRoomName(input.getRoomName());
        result.setRoomSetup(input.getRoomName().startsWith("Room")
                ? "Cinema"
                : input.getRoomName().startsWith("BOF")
                ? "BOF"
                : "Any Other"); // reversed alpha numeric sort
        result.setSlotId(Integer.toString(input.getTalkId()));
        result.setToTime(Instant.parse(input.getToDate()).atZone(ZoneId.systemDefault()).format(HOUR_MINUTE));
        result.setToTimeMillis(Instant.parse(input.getToDate()).toEpochMilli());

        if (input.isSessionTypeBreak()) {
            result.setBreak(new Break());
            result.getBreak().setDayName(result.getDay());
            result.getBreak().setEndTime(input.getToDate());
            result.getBreak().setId(result.getSlotId());
            result.getBreak().setNameEN(input.getTalkTitle());
            result.getBreak().setNameFR(null);

            final Room room = new Room();
//            room.setCapacity(-1);
            room.setId(result.getRoomId());
            room.setName(result.getRoomName());
//            room.setRecorded(null);
//            room.setSetup(null);
            result.getBreak().setRoom(room);
            result.getBreak().setStartTime(input.getFromDate());
        } else {
            result.setTalk(convertToTalk(input));
        }

        return result;
    }

    private Schedule convertToSchedule(final List<V2TimeSlot> input) {
        final Schedule result = new Schedule();

        result.setSlots(input.stream()
                .map(this::convertToScheduleSlot)
                .collect(Collectors.toList()));

        return result;
    }

    private Speaker convertToSpeaker(final V2Speaker input, final boolean complete) {
        final Speaker result = new SpeakerV2();

        result.setAcceptedTalks(input.getProposals().stream()
                .map(this::convertToTalk)
                .collect(Collectors.toList()));
        result.setAvatarURL(input.getImageUrl());
        result.setBio(input.getBio());
//        result.setBioAsHtml(input.);
//        result.setBlog(input.);
        result.setCompany(input.getCompany());
        result.setFirstName(input.getFirstName());
//        result.setLang(input.);
        result.setLastName(input.getLastName());
//        result.setLinks(input.);
        result.setTwitter(input.getTwitterHandle());
        // V2 does not have a UUID. CFPClientTestBase however requires one for test 'speakerIsRetrievable'
        result.setUuid(Integer.toString(input.getId()));

        if (!complete) {
            result.setLinks(Collections.singletonList(createSpeakerLink(input)));
        }

        return result;
    }

    private SpeakerReference convertToSpeakerReference(final V2Speaker input) {
        final SpeakerReference result = new SpeakerReferenceV2();

        result.setLink(createSpeakerLink(input));
        result.setName(input.getFirstName() + ' ' + input.getLastName());

        return result;
    }

    private Tag convertToTag(final V2Tag input) {
        final Tag result = new Tag();

        result.setValue(input.getName());

        return result;
    }

    private Talk convertToTalk(final V2Proposal input) {
        final Talk result = new TalkV2();

//        result.setAudienceLevel(input.);
        result.setId(Integer.toString(input.getId()));
//        result.setLang(input.);
//        result.setLinks(input.);
        result.setSpeakers(input.getSpeakers().stream()
                .map(this::convertToSpeakerReference)
                .collect(Collectors.toList()));
        result.setSummary(input.getDescription());
//        result.setSummaryAsHtml(input.);
        result.setTags(input.getTags().stream()
                .map(this::convertToTag)
                .collect(Collectors.toList()));
        result.setTalkType(input.getSessionTypeName());
        result.setTitle(input.getTitle());
        result.setTrack(input.getTrackName());
        result.setTrackId(Integer.toString(input.getTrackId()));
        result.setVideoURL(input.getAfterVideoURL());

        result.setLinks(Collections.singletonList(createTalkLink(
                eventUri + "/talks/" + input.getId(),
                input.getTitle())));

        return result;
    }

    private Talk convertToTalk(final V2Talk input) {
        final Talk result = new TalkV2();

//        result.setAudienceLevel(input.);
        result.setId(Integer.toString(input.getId()));
//        result.setLang(input.);
//        result.setLinks(input.);
        result.setSpeakers(input.getSpeakers().stream()
                .map(this::convertToSpeakerReference)
                .collect(Collectors.toList()));
        result.setSummary(input.getDescription());
//        result.setSummaryAsHtml(input.);
        result.setTags(input.getTags().stream()
                .map(this::convertToTag)
                .collect(Collectors.toList()));
        result.setTalkType(input.getSessionTypeName());
        result.setTitle(input.getTitle());
        result.setTrack(input.getTrackName());
        result.setTrackId(Integer.toString(input.getTrackId()));
        result.setVideoURL(input.getVideoURL());

        return result;
    }

    private Talk convertToTalk(final V2TimeSlot input) {
        final Talk result = new TalkV2();

//        result.setAudienceLevel(input.);
        result.setId(Integer.toString(input.getTalkId()));
//        result.setLang(input.);
//        result.setLinks(input.);
        result.setSpeakers(input.getSpeakers().stream()
                .map(this::convertToSpeakerReference)
                .collect(Collectors.toList()));
        result.setSummary(input.getTalkDescription());
//        result.setSummaryAsHtml(input.);
        result.setTags(input.getTags().stream()
                .map(this::convertToTag)
                .collect(Collectors.toList()));
        result.setTalkType(input.getSessionTypeName());
        result.setTitle(input.getTalkTitle());
        result.setTrack(input.getTrackName());
//        result.setTrackId(Integer.toString(input.getTrackId()));
//        result.setVideoURL(input.getAfterVideoURL());

        return result;
    }

    private Track convertToTrack(final V2Track input) {
        final Track result = new Track();

        result.setDescription(input.getDescription());
        result.setId(Integer.toString(input.getId()));
        result.setImgsrc(input.getImageURL());
        result.setTitle(input.getName());

        return result;
    }

    private Link createLink(final String rel, final String href, final String title) {
        final Link result = new Link();

        result.setHref(href);
        // Workaround for V2 not having rel and typed links
        result.setRel(eventUri + "/dummy" + rel);
        result.setTitle(title);

        return result;
    }

    private Link createScheduleLink(final V2Link input) {
        return createLink(
                "/api/profile/schedule",
                input.getHref(),
                input.getTitle());
    }

    private Link createSpeakerLink(final V2Speaker input) {
        return createLink(
                "/api/profile/speaker",
                eventUri + "/speakers/" + input.getId(),
                input.getFirstName() + ' ' + input.getLastName());
    }

    private Link createTalkLink(final String href, final String title) {
        return createLink(
                "/api/profile/talk",
                href,
                title);
    }

    private class SpeakerV2 extends Speaker {

        @Override
        public Optional<Speaker> reload() {
            if (hasCompleteInformation()) {
                return Optional.of(this);
            } else {
                return getLinkStream(Link.Type.SPEAKER)
                        .findAny()
                        .flatMap(link
                                -> readOptionalFrom(link.getHref(), V2Speaker.class)
                                .map(o -> convertToSpeaker(o, true)));
            }
        }
    }

    private class SpeakerReferenceV2 extends SpeakerReference {

        @Override
        public Optional<Speaker> getSpeaker() {
            return readOptionalFrom(getLink().getHref(), V2Speaker.class)
                    .map(o -> convertToSpeaker(o, true));
        }
    }

    private class TalkV2 extends Talk {

        @Override
        public Optional<Talk> reload() {
            if (hasCompleteInformation()) {
                return Optional.of(this);
            } else {
                return getLinkStream(Link.Type.TALK)
                        .findAny()
                        .flatMap(link
                                -> readOptionalFrom(link.getHref(), V2Talk.class)
                                .map(Devoxx2019BECFPClient.this::convertToTalk));
            }
        }
    }
}
