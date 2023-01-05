package com.retailer.invoice.web.rest;

import com.retailer.invoice.domain.Media;
import com.retailer.invoice.repository.MediaRepository;
import com.retailer.invoice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.retailer.invoice.domain.Media}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MediaResource {

    private final Logger log = LoggerFactory.getLogger(MediaResource.class);

    private static final String ENTITY_NAME = "media";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MediaRepository mediaRepository;

    public MediaResource(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    /**
     * {@code POST  /media} : Create a new media.
     *
     * @param media the media to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new media, or with status {@code 400 (Bad Request)} if the media has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/media")
    public ResponseEntity<Media> createMedia(@Valid @RequestBody Media media) throws URISyntaxException {
        log.debug("REST request to save Media : {}", media);
        if (media.getMediaId() != null) {
            throw new BadRequestAlertException("A new media cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Media result = mediaRepository.save(media);
        return ResponseEntity
            .created(new URI("/api/media/" + result.getMediaId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getMediaId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /media/:mediaId} : Updates an existing media.
     *
     * @param mediaId the id of the media to save.
     * @param media the media to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated media,
     * or with status {@code 400 (Bad Request)} if the media is not valid,
     * or with status {@code 500 (Internal Server Error)} if the media couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/media/{mediaId}")
    public ResponseEntity<Media> updateMedia(
        @PathVariable(value = "mediaId", required = false) final Long mediaId,
        @Valid @RequestBody Media media
    ) throws URISyntaxException {
        log.debug("REST request to update Media : {}, {}", mediaId, media);
        if (media.getMediaId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(mediaId, media.getMediaId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mediaRepository.existsById(mediaId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Media result = mediaRepository.save(media);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, media.getMediaId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /media/:mediaId} : Partial updates given fields of an existing media, field will ignore if it is null
     *
     * @param mediaId the id of the media to save.
     * @param media the media to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated media,
     * or with status {@code 400 (Bad Request)} if the media is not valid,
     * or with status {@code 404 (Not Found)} if the media is not found,
     * or with status {@code 500 (Internal Server Error)} if the media couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/media/{mediaId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Media> partialUpdateMedia(
        @PathVariable(value = "mediaId", required = false) final Long mediaId,
        @NotNull @RequestBody Media media
    ) throws URISyntaxException {
        log.debug("REST request to partial update Media partially : {}, {}", mediaId, media);
        if (media.getMediaId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(mediaId, media.getMediaId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mediaRepository.existsById(mediaId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Media> result = mediaRepository
            .findById(media.getMediaId())
            .map(existingMedia -> {
                if (media.getPath() != null) {
                    existingMedia.setPath(media.getPath());
                }
                if (media.getType() != null) {
                    existingMedia.setType(media.getType());
                }

                return existingMedia;
            })
            .map(mediaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, media.getMediaId().toString())
        );
    }

    /**
     * {@code GET  /media} : get all the media.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of media in body.
     */
    @GetMapping("/media")
    public List<Media> getAllMedia() {
        log.debug("REST request to get all Media");
        return mediaRepository.findAll();
    }

    /**
     * {@code GET  /media/:id} : get the "id" media.
     *
     * @param id the id of the media to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the media, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/media/{id}")
    public ResponseEntity<Media> getMedia(@PathVariable Long id) {
        log.debug("REST request to get Media : {}", id);
        Optional<Media> media = mediaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(media);
    }

    /**
     * {@code DELETE  /media/:id} : delete the "id" media.
     *
     * @param id the id of the media to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/media/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) {
        log.debug("REST request to delete Media : {}", id);
        mediaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
