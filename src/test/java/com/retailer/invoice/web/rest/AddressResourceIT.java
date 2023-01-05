package com.retailer.invoice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.retailer.invoice.IntegrationTest;
import com.retailer.invoice.domain.Address;
import com.retailer.invoice.repository.AddressRepository;
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
 * Integration tests for the {@link AddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AddressResourceIT {

    private static final String DEFAULT_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final Long DEFAULT_PINCODE = 1L;
    private static final Long UPDATED_PINCODE = 2L;

    private static final Float DEFAULT_LAT = 1F;
    private static final Float UPDATED_LAT = 2F;

    private static final Float DEFAULT_LON = 1F;
    private static final Float UPDATED_LON = 2F;

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_LANDMARK = "AAAAAAAAAA";
    private static final String UPDATED_LANDMARK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SHIPPING_ADDRESS = false;
    private static final Boolean UPDATED_SHIPPING_ADDRESS = true;

    private static final Boolean DEFAULT_BILLING_ADDRESS = false;
    private static final Boolean UPDATED_BILLING_ADDRESS = true;

    private static final Boolean DEFAULT_HOME_ADDRESS = false;
    private static final Boolean UPDATED_HOME_ADDRESS = true;

    private static final Boolean DEFAULT_OFFICE_ADDRESS = false;
    private static final Boolean UPDATED_OFFICE_ADDRESS = true;

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAddressMockMvc;

    private Address address;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createEntity(EntityManager em) {
        Address address = new Address()
            .line1(DEFAULT_LINE_1)
            .line2(DEFAULT_LINE_2)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .pincode(DEFAULT_PINCODE)
            .lat(DEFAULT_LAT)
            .lon(DEFAULT_LON)
            .country(DEFAULT_COUNTRY)
            .landmark(DEFAULT_LANDMARK)
            .shippingAddress(DEFAULT_SHIPPING_ADDRESS)
            .billingAddress(DEFAULT_BILLING_ADDRESS)
            .homeAddress(DEFAULT_HOME_ADDRESS)
            .officeAddress(DEFAULT_OFFICE_ADDRESS)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedOn(DEFAULT_UPDATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return address;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createUpdatedEntity(EntityManager em) {
        Address address = new Address()
            .line1(UPDATED_LINE_1)
            .line2(UPDATED_LINE_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .pincode(UPDATED_PINCODE)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .country(UPDATED_COUNTRY)
            .landmark(UPDATED_LANDMARK)
            .shippingAddress(UPDATED_SHIPPING_ADDRESS)
            .billingAddress(UPDATED_BILLING_ADDRESS)
            .homeAddress(UPDATED_HOME_ADDRESS)
            .officeAddress(UPDATED_OFFICE_ADDRESS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return address;
    }

    @BeforeEach
    public void initTest() {
        address = createEntity(em);
    }

    @Test
    @Transactional
    void createAddress() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();
        // Create the Address
        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(address)))
            .andExpect(status().isCreated());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate + 1);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getLine1()).isEqualTo(DEFAULT_LINE_1);
        assertThat(testAddress.getLine2()).isEqualTo(DEFAULT_LINE_2);
        assertThat(testAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testAddress.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testAddress.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testAddress.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testAddress.getLon()).isEqualTo(DEFAULT_LON);
        assertThat(testAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testAddress.getLandmark()).isEqualTo(DEFAULT_LANDMARK);
        assertThat(testAddress.getShippingAddress()).isEqualTo(DEFAULT_SHIPPING_ADDRESS);
        assertThat(testAddress.getBillingAddress()).isEqualTo(DEFAULT_BILLING_ADDRESS);
        assertThat(testAddress.getHomeAddress()).isEqualTo(DEFAULT_HOME_ADDRESS);
        assertThat(testAddress.getOfficeAddress()).isEqualTo(DEFAULT_OFFICE_ADDRESS);
        assertThat(testAddress.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testAddress.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testAddress.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAddress.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createAddressWithExistingId() throws Exception {
        // Create the Address with an existing ID
        address.setId(1L);

        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(address)))
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAddresses() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
            .andExpect(jsonPath("$.[*].line1").value(hasItem(DEFAULT_LINE_1)))
            .andExpect(jsonPath("$.[*].line2").value(hasItem(DEFAULT_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE.intValue())))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].landmark").value(hasItem(DEFAULT_LANDMARK)))
            .andExpect(jsonPath("$.[*].shippingAddress").value(hasItem(DEFAULT_SHIPPING_ADDRESS.booleanValue())))
            .andExpect(jsonPath("$.[*].billingAddress").value(hasItem(DEFAULT_BILLING_ADDRESS.booleanValue())))
            .andExpect(jsonPath("$.[*].homeAddress").value(hasItem(DEFAULT_HOME_ADDRESS.booleanValue())))
            .andExpect(jsonPath("$.[*].officeAddress").value(hasItem(DEFAULT_OFFICE_ADDRESS.booleanValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get the address
        restAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(address.getId().intValue()))
            .andExpect(jsonPath("$.line1").value(DEFAULT_LINE_1))
            .andExpect(jsonPath("$.line2").value(DEFAULT_LINE_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.pincode").value(DEFAULT_PINCODE.intValue()))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lon").value(DEFAULT_LON.doubleValue()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.landmark").value(DEFAULT_LANDMARK))
            .andExpect(jsonPath("$.shippingAddress").value(DEFAULT_SHIPPING_ADDRESS.booleanValue()))
            .andExpect(jsonPath("$.billingAddress").value(DEFAULT_BILLING_ADDRESS.booleanValue()))
            .andExpect(jsonPath("$.homeAddress").value(DEFAULT_HOME_ADDRESS.booleanValue()))
            .andExpect(jsonPath("$.officeAddress").value(DEFAULT_OFFICE_ADDRESS.booleanValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingAddress() throws Exception {
        // Get the address
        restAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address
        Address updatedAddress = addressRepository.findById(address.getId()).get();
        // Disconnect from session so that the updates on updatedAddress are not directly saved in db
        em.detach(updatedAddress);
        updatedAddress
            .line1(UPDATED_LINE_1)
            .line2(UPDATED_LINE_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .pincode(UPDATED_PINCODE)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .country(UPDATED_COUNTRY)
            .landmark(UPDATED_LANDMARK)
            .shippingAddress(UPDATED_SHIPPING_ADDRESS)
            .billingAddress(UPDATED_BILLING_ADDRESS)
            .homeAddress(UPDATED_HOME_ADDRESS)
            .officeAddress(UPDATED_OFFICE_ADDRESS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAddress.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAddress))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getLine1()).isEqualTo(UPDATED_LINE_1);
        assertThat(testAddress.getLine2()).isEqualTo(UPDATED_LINE_2);
        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAddress.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testAddress.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testAddress.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testAddress.getLandmark()).isEqualTo(UPDATED_LANDMARK);
        assertThat(testAddress.getShippingAddress()).isEqualTo(UPDATED_SHIPPING_ADDRESS);
        assertThat(testAddress.getBillingAddress()).isEqualTo(UPDATED_BILLING_ADDRESS);
        assertThat(testAddress.getHomeAddress()).isEqualTo(UPDATED_HOME_ADDRESS);
        assertThat(testAddress.getOfficeAddress()).isEqualTo(UPDATED_OFFICE_ADDRESS);
        assertThat(testAddress.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testAddress.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testAddress.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAddress.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, address.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(address))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(address))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(address)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAddressWithPatch() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address using partial update
        Address partialUpdatedAddress = new Address();
        partialUpdatedAddress.setId(address.getId());

        partialUpdatedAddress
            .line2(UPDATED_LINE_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .pincode(UPDATED_PINCODE)
            .lon(UPDATED_LON)
            .country(UPDATED_COUNTRY)
            .billingAddress(UPDATED_BILLING_ADDRESS);

        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddress))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getLine1()).isEqualTo(DEFAULT_LINE_1);
        assertThat(testAddress.getLine2()).isEqualTo(UPDATED_LINE_2);
        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAddress.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testAddress.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testAddress.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testAddress.getLandmark()).isEqualTo(DEFAULT_LANDMARK);
        assertThat(testAddress.getShippingAddress()).isEqualTo(DEFAULT_SHIPPING_ADDRESS);
        assertThat(testAddress.getBillingAddress()).isEqualTo(UPDATED_BILLING_ADDRESS);
        assertThat(testAddress.getHomeAddress()).isEqualTo(DEFAULT_HOME_ADDRESS);
        assertThat(testAddress.getOfficeAddress()).isEqualTo(DEFAULT_OFFICE_ADDRESS);
        assertThat(testAddress.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testAddress.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testAddress.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAddress.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateAddressWithPatch() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address using partial update
        Address partialUpdatedAddress = new Address();
        partialUpdatedAddress.setId(address.getId());

        partialUpdatedAddress
            .line1(UPDATED_LINE_1)
            .line2(UPDATED_LINE_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .pincode(UPDATED_PINCODE)
            .lat(UPDATED_LAT)
            .lon(UPDATED_LON)
            .country(UPDATED_COUNTRY)
            .landmark(UPDATED_LANDMARK)
            .shippingAddress(UPDATED_SHIPPING_ADDRESS)
            .billingAddress(UPDATED_BILLING_ADDRESS)
            .homeAddress(UPDATED_HOME_ADDRESS)
            .officeAddress(UPDATED_OFFICE_ADDRESS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddress))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getLine1()).isEqualTo(UPDATED_LINE_1);
        assertThat(testAddress.getLine2()).isEqualTo(UPDATED_LINE_2);
        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAddress.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testAddress.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testAddress.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testAddress.getLandmark()).isEqualTo(UPDATED_LANDMARK);
        assertThat(testAddress.getShippingAddress()).isEqualTo(UPDATED_SHIPPING_ADDRESS);
        assertThat(testAddress.getBillingAddress()).isEqualTo(UPDATED_BILLING_ADDRESS);
        assertThat(testAddress.getHomeAddress()).isEqualTo(UPDATED_HOME_ADDRESS);
        assertThat(testAddress.getOfficeAddress()).isEqualTo(UPDATED_OFFICE_ADDRESS);
        assertThat(testAddress.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testAddress.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testAddress.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAddress.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, address.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(address))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(address))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(address)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeDelete = addressRepository.findAll().size();

        // Delete the address
        restAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, address.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
