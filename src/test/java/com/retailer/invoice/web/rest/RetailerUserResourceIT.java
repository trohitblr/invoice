package com.retailer.invoice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.retailer.invoice.IntegrationTest;
import com.retailer.invoice.domain.RetailerUser;
import com.retailer.invoice.domain.enumeration.UserType;
import com.retailer.invoice.repository.RetailerUserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RetailerUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RetailerUserResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_PHONE = 1L;
    private static final Long UPDATED_PHONE = 2L;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final UserType DEFAULT_TYPE = UserType.OWNER;
    private static final UserType UPDATED_TYPE = UserType.ADMIN;

    private static final String DEFAULT_ENC_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_ENC_PASSWORD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/retailer-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{retailerUserId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RetailerUserRepository retailerUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRetailerUserMockMvc;

    private RetailerUser retailerUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RetailerUser createEntity(EntityManager em) {
        RetailerUser retailerUser = new RetailerUser()
            .userId(DEFAULT_USER_ID)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .type(DEFAULT_TYPE)
            .encPassword(DEFAULT_ENC_PASSWORD)
            .status(DEFAULT_STATUS)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedOn(DEFAULT_UPDATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return retailerUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RetailerUser createUpdatedEntity(EntityManager em) {
        RetailerUser retailerUser = new RetailerUser()
            .userId(UPDATED_USER_ID)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .type(UPDATED_TYPE)
            .encPassword(UPDATED_ENC_PASSWORD)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return retailerUser;
    }

    @BeforeEach
    public void initTest() {
        retailerUser = createEntity(em);
    }

    @Test
    @Transactional
    void createRetailerUser() throws Exception {
        int databaseSizeBeforeCreate = retailerUserRepository.findAll().size();
        // Create the RetailerUser
        restRetailerUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retailerUser)))
            .andExpect(status().isCreated());

        // Validate the RetailerUser in the database
        List<RetailerUser> retailerUserList = retailerUserRepository.findAll();
        assertThat(retailerUserList).hasSize(databaseSizeBeforeCreate + 1);
        RetailerUser testRetailerUser = retailerUserList.get(retailerUserList.size() - 1);
        assertThat(testRetailerUser.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testRetailerUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testRetailerUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testRetailerUser.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRetailerUser.getEncPassword()).isEqualTo(DEFAULT_ENC_PASSWORD);
        assertThat(testRetailerUser.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRetailerUser.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testRetailerUser.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testRetailerUser.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRetailerUser.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createRetailerUserWithExistingId() throws Exception {
        // Create the RetailerUser with an existing ID
        retailerUser.setRetailerUserId(1L);

        int databaseSizeBeforeCreate = retailerUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRetailerUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retailerUser)))
            .andExpect(status().isBadRequest());

        // Validate the RetailerUser in the database
        List<RetailerUser> retailerUserList = retailerUserRepository.findAll();
        assertThat(retailerUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRetailerUsers() throws Exception {
        // Initialize the database
        retailerUserRepository.saveAndFlush(retailerUser);

        // Get all the retailerUserList
        restRetailerUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=retailerUserId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].retailerUserId").value(hasItem(retailerUser.getRetailerUserId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].encPassword").value(hasItem(DEFAULT_ENC_PASSWORD)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getRetailerUser() throws Exception {
        // Initialize the database
        retailerUserRepository.saveAndFlush(retailerUser);

        // Get the retailerUser
        restRetailerUserMockMvc
            .perform(get(ENTITY_API_URL_ID, retailerUser.getRetailerUserId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.retailerUserId").value(retailerUser.getRetailerUserId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.encPassword").value(DEFAULT_ENC_PASSWORD))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingRetailerUser() throws Exception {
        // Get the retailerUser
        restRetailerUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRetailerUser() throws Exception {
        // Initialize the database
        retailerUserRepository.saveAndFlush(retailerUser);

        int databaseSizeBeforeUpdate = retailerUserRepository.findAll().size();

        // Update the retailerUser
        RetailerUser updatedRetailerUser = retailerUserRepository.findById(retailerUser.getRetailerUserId()).get();
        // Disconnect from session so that the updates on updatedRetailerUser are not directly saved in db
        em.detach(updatedRetailerUser);
        updatedRetailerUser
            .userId(UPDATED_USER_ID)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .type(UPDATED_TYPE)
            .encPassword(UPDATED_ENC_PASSWORD)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restRetailerUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRetailerUser.getRetailerUserId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRetailerUser))
            )
            .andExpect(status().isOk());

        // Validate the RetailerUser in the database
        List<RetailerUser> retailerUserList = retailerUserRepository.findAll();
        assertThat(retailerUserList).hasSize(databaseSizeBeforeUpdate);
        RetailerUser testRetailerUser = retailerUserList.get(retailerUserList.size() - 1);
        assertThat(testRetailerUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRetailerUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testRetailerUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRetailerUser.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRetailerUser.getEncPassword()).isEqualTo(UPDATED_ENC_PASSWORD);
        assertThat(testRetailerUser.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRetailerUser.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRetailerUser.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testRetailerUser.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRetailerUser.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingRetailerUser() throws Exception {
        int databaseSizeBeforeUpdate = retailerUserRepository.findAll().size();
        retailerUser.setRetailerUserId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRetailerUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, retailerUser.getRetailerUserId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retailerUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetailerUser in the database
        List<RetailerUser> retailerUserList = retailerUserRepository.findAll();
        assertThat(retailerUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRetailerUser() throws Exception {
        int databaseSizeBeforeUpdate = retailerUserRepository.findAll().size();
        retailerUser.setRetailerUserId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetailerUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retailerUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetailerUser in the database
        List<RetailerUser> retailerUserList = retailerUserRepository.findAll();
        assertThat(retailerUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRetailerUser() throws Exception {
        int databaseSizeBeforeUpdate = retailerUserRepository.findAll().size();
        retailerUser.setRetailerUserId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetailerUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retailerUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RetailerUser in the database
        List<RetailerUser> retailerUserList = retailerUserRepository.findAll();
        assertThat(retailerUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRetailerUserWithPatch() throws Exception {
        // Initialize the database
        retailerUserRepository.saveAndFlush(retailerUser);

        int databaseSizeBeforeUpdate = retailerUserRepository.findAll().size();

        // Update the retailerUser using partial update
        RetailerUser partialUpdatedRetailerUser = new RetailerUser();
        partialUpdatedRetailerUser.setRetailerUserId(retailerUser.getRetailerUserId());

        partialUpdatedRetailerUser
            .userId(UPDATED_USER_ID)
            .phone(UPDATED_PHONE)
            .encPassword(UPDATED_ENC_PASSWORD)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restRetailerUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRetailerUser.getRetailerUserId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRetailerUser))
            )
            .andExpect(status().isOk());

        // Validate the RetailerUser in the database
        List<RetailerUser> retailerUserList = retailerUserRepository.findAll();
        assertThat(retailerUserList).hasSize(databaseSizeBeforeUpdate);
        RetailerUser testRetailerUser = retailerUserList.get(retailerUserList.size() - 1);
        assertThat(testRetailerUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRetailerUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testRetailerUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testRetailerUser.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRetailerUser.getEncPassword()).isEqualTo(UPDATED_ENC_PASSWORD);
        assertThat(testRetailerUser.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRetailerUser.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRetailerUser.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testRetailerUser.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRetailerUser.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateRetailerUserWithPatch() throws Exception {
        // Initialize the database
        retailerUserRepository.saveAndFlush(retailerUser);

        int databaseSizeBeforeUpdate = retailerUserRepository.findAll().size();

        // Update the retailerUser using partial update
        RetailerUser partialUpdatedRetailerUser = new RetailerUser();
        partialUpdatedRetailerUser.setRetailerUserId(retailerUser.getRetailerUserId());

        partialUpdatedRetailerUser
            .userId(UPDATED_USER_ID)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .type(UPDATED_TYPE)
            .encPassword(UPDATED_ENC_PASSWORD)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restRetailerUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRetailerUser.getRetailerUserId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRetailerUser))
            )
            .andExpect(status().isOk());

        // Validate the RetailerUser in the database
        List<RetailerUser> retailerUserList = retailerUserRepository.findAll();
        assertThat(retailerUserList).hasSize(databaseSizeBeforeUpdate);
        RetailerUser testRetailerUser = retailerUserList.get(retailerUserList.size() - 1);
        assertThat(testRetailerUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRetailerUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testRetailerUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRetailerUser.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRetailerUser.getEncPassword()).isEqualTo(UPDATED_ENC_PASSWORD);
        assertThat(testRetailerUser.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRetailerUser.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRetailerUser.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testRetailerUser.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRetailerUser.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingRetailerUser() throws Exception {
        int databaseSizeBeforeUpdate = retailerUserRepository.findAll().size();
        retailerUser.setRetailerUserId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRetailerUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, retailerUser.getRetailerUserId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retailerUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetailerUser in the database
        List<RetailerUser> retailerUserList = retailerUserRepository.findAll();
        assertThat(retailerUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRetailerUser() throws Exception {
        int databaseSizeBeforeUpdate = retailerUserRepository.findAll().size();
        retailerUser.setRetailerUserId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetailerUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retailerUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetailerUser in the database
        List<RetailerUser> retailerUserList = retailerUserRepository.findAll();
        assertThat(retailerUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRetailerUser() throws Exception {
        int databaseSizeBeforeUpdate = retailerUserRepository.findAll().size();
        retailerUser.setRetailerUserId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetailerUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(retailerUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RetailerUser in the database
        List<RetailerUser> retailerUserList = retailerUserRepository.findAll();
        assertThat(retailerUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRetailerUser() throws Exception {
        // Initialize the database
        retailerUserRepository.saveAndFlush(retailerUser);

        int databaseSizeBeforeDelete = retailerUserRepository.findAll().size();

        // Delete the retailerUser
        restRetailerUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, retailerUser.getRetailerUserId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RetailerUser> retailerUserList = retailerUserRepository.findAll();
        assertThat(retailerUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
