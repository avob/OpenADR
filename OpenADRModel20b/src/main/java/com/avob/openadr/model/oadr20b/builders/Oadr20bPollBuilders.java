package com.avob.openadr.model.oadr20b.builders;

import com.avob.openadr.model.oadr20b.builders.poll.Oadr20bPollBuilder;

public class Oadr20bPollBuilders {

    private Oadr20bPollBuilders() {
    }

    public static Oadr20bPollBuilder newOadr20bPollBuilder(String venId) {
        return new Oadr20bPollBuilder(venId);
    }
}
