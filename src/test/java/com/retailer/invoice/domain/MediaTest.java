package com.retailer.invoice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.retailer.invoice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MediaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Media.class);
        Media media1 = new Media();
        media1.setMediaId(1L);
        Media media2 = new Media();
        media2.setMediaId(media1.getMediaId());
        assertThat(media1).isEqualTo(media2);
        media2.setMediaId(2L);
        assertThat(media1).isNotEqualTo(media2);
        media1.setMediaId(null);
        assertThat(media1).isNotEqualTo(media2);
    }
}
