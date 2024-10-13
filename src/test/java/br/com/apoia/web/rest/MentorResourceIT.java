package br.com.apoia.web.rest;

import static br.com.apoia.domain.MentorAsserts.*;
import static br.com.apoia.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.apoia.IntegrationTest;
import br.com.apoia.domain.Mentor;
import br.com.apoia.repository.MentorRepository;
import br.com.apoia.service.dto.MentorDTO;
import br.com.apoia.service.mapper.MentorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MentorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MentorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mentors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private MentorMapper mentorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMentorMockMvc;

    private Mentor mentor;

    private Mentor insertedMentor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mentor createEntity() {
        return new Mentor().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mentor createUpdatedEntity() {
        return new Mentor().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        mentor = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMentor != null) {
            mentorRepository.delete(insertedMentor);
            insertedMentor = null;
        }
    }

    @Test
    @Transactional
    void createMentor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Mentor
        MentorDTO mentorDTO = mentorMapper.toDto(mentor);
        var returnedMentorDTO = om.readValue(
            restMentorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mentorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MentorDTO.class
        );

        // Validate the Mentor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMentor = mentorMapper.toEntity(returnedMentorDTO);
        assertMentorUpdatableFieldsEquals(returnedMentor, getPersistedMentor(returnedMentor));

        insertedMentor = returnedMentor;
    }

    @Test
    @Transactional
    void createMentorWithExistingId() throws Exception {
        // Create the Mentor with an existing ID
        insertedMentor = mentorRepository.saveAndFlush(mentor);
        MentorDTO mentorDTO = mentorMapper.toDto(mentor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMentorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mentorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Mentor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMentors() throws Exception {
        // Initialize the database
        insertedMentor = mentorRepository.saveAndFlush(mentor);

        // Get all the mentorList
        restMentorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mentor.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getMentor() throws Exception {
        // Initialize the database
        insertedMentor = mentorRepository.saveAndFlush(mentor);

        // Get the mentor
        restMentorMockMvc
            .perform(get(ENTITY_API_URL_ID, mentor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mentor.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingMentor() throws Exception {
        // Get the mentor
        restMentorMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMentor() throws Exception {
        // Initialize the database
        insertedMentor = mentorRepository.saveAndFlush(mentor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mentor
        Mentor updatedMentor = mentorRepository.findById(mentor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMentor are not directly saved in db
        em.detach(updatedMentor);
        updatedMentor.name(UPDATED_NAME);
        MentorDTO mentorDTO = mentorMapper.toDto(updatedMentor);

        restMentorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mentorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mentorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Mentor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMentorToMatchAllProperties(updatedMentor);
    }

    @Test
    @Transactional
    void putNonExistingMentor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mentor.setId(UUID.randomUUID());

        // Create the Mentor
        MentorDTO mentorDTO = mentorMapper.toDto(mentor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMentorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mentorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mentorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mentor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMentor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mentor.setId(UUID.randomUUID());

        // Create the Mentor
        MentorDTO mentorDTO = mentorMapper.toDto(mentor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMentorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mentorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mentor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMentor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mentor.setId(UUID.randomUUID());

        // Create the Mentor
        MentorDTO mentorDTO = mentorMapper.toDto(mentor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMentorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mentorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mentor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMentorWithPatch() throws Exception {
        // Initialize the database
        insertedMentor = mentorRepository.saveAndFlush(mentor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mentor using partial update
        Mentor partialUpdatedMentor = new Mentor();
        partialUpdatedMentor.setId(mentor.getId());

        partialUpdatedMentor.name(UPDATED_NAME);

        restMentorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMentor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMentor))
            )
            .andExpect(status().isOk());

        // Validate the Mentor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMentorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMentor, mentor), getPersistedMentor(mentor));
    }

    @Test
    @Transactional
    void fullUpdateMentorWithPatch() throws Exception {
        // Initialize the database
        insertedMentor = mentorRepository.saveAndFlush(mentor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mentor using partial update
        Mentor partialUpdatedMentor = new Mentor();
        partialUpdatedMentor.setId(mentor.getId());

        partialUpdatedMentor.name(UPDATED_NAME);

        restMentorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMentor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMentor))
            )
            .andExpect(status().isOk());

        // Validate the Mentor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMentorUpdatableFieldsEquals(partialUpdatedMentor, getPersistedMentor(partialUpdatedMentor));
    }

    @Test
    @Transactional
    void patchNonExistingMentor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mentor.setId(UUID.randomUUID());

        // Create the Mentor
        MentorDTO mentorDTO = mentorMapper.toDto(mentor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMentorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mentorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mentorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mentor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMentor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mentor.setId(UUID.randomUUID());

        // Create the Mentor
        MentorDTO mentorDTO = mentorMapper.toDto(mentor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMentorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mentorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mentor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMentor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mentor.setId(UUID.randomUUID());

        // Create the Mentor
        MentorDTO mentorDTO = mentorMapper.toDto(mentor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMentorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mentorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mentor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMentor() throws Exception {
        // Initialize the database
        insertedMentor = mentorRepository.saveAndFlush(mentor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the mentor
        restMentorMockMvc
            .perform(delete(ENTITY_API_URL_ID, mentor.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mentorRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Mentor getPersistedMentor(Mentor mentor) {
        return mentorRepository.findById(mentor.getId()).orElseThrow();
    }

    protected void assertPersistedMentorToMatchAllProperties(Mentor expectedMentor) {
        assertMentorAllPropertiesEquals(expectedMentor, getPersistedMentor(expectedMentor));
    }

    protected void assertPersistedMentorToMatchUpdatableProperties(Mentor expectedMentor) {
        assertMentorAllUpdatablePropertiesEquals(expectedMentor, getPersistedMentor(expectedMentor));
    }
}
