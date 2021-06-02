<template>
  <div class="m-2">

    <b-breadcrumb :items="items"/>

    <div v-if="ready">

      <b-form @submit.prevent="save" @reset.prevent="reset"><br />

        <h1 v-if="isEdit" class="text-primary">Edit Provider {{ getValue('id') }}</h1>
        <h1 v-else class="text-primary">Add New Provider</h1>
        <br />

        <div>
          <b-card>

            <b-col sm="6">
              <label :for="orgNameId">Organization Name:</label>
              <b-form-input
                :id="orgNameId"
                type="text" @blur="() => setTouched({path: 'organizationName', touched: true})"
                :value="getValue('organizationName')"
                @update="(value) => setValue({ path: 'organizationName', value })"
                :state="showError('organizationName')"
              />
              <b-form-invalid-feedback>{{ getError('organizationName') }}</b-form-invalid-feedback>
            </b-col><br />

            <b-col sm="6">
              <label :for="enabledId">Enabled:</label>
              <b-form-select
                :id="enabledId"
                :value="getValue('enabled')"
                :options="[{ item: true, name: 'Enabled' }, { item: false, name: 'Disabled' }]"
                @change="(value) => setValue({ path: 'enabled', value })"
                @blur.native="() => setTouched({path: 'enabled', touched: true})"
                value-field="item"
                text-field="name"
              ></b-form-select>
            </b-col><br />

            <b-col sm="6">
              <label :for="uIdPrefixId">Unique Id Prefix:</label>
              <b-form-input
                :id="uIdPrefixId"
                type="text" @blur="() => setTouched({path: 'uniqueIdPrefix', touched: true})"
                :value="getValue('uniqueIdPrefix')"
                @update="(value) => setValue({ path: 'uniqueIdPrefix', value })"
                :state="showError('uniqueIdPrefix')"
              />
              <b-form-invalid-feedback>{{ getError('uniqueIdPrefix') }}</b-form-invalid-feedback>
            </b-col><br />

            <b-col sm="6">
              <label :for="websiteId">Website:</label>
              <b-form-input
                :id="websiteId"
                type="text" @blur="() => setTouched({path: 'website', touched: true})"
                :value="getValue('website')"
                @update="(value) => setValue({ path: 'website', value })"
                :state="showError('website')"
              />
              <b-form-invalid-feedback>{{ getError('website') }}</b-form-invalid-feedback>
            </b-col><br />

          </b-card>
        </div>

        <br />

        <div>
          <b-card>
            <h2 class="text-secondary">Tokens:</h2>

            <div>
              <b-button-group size="md">
                <b-button variant="outline-primary" @click="addNewToken">
                  <b-icon icon="key-fill" class="ml-2"></b-icon> Add New Token
                </b-button>
              </b-button-group>
            </div>

            <span v-for="(token, index) in getValue('tokens')" :key="'A'+ index">
              <br />
              <b-button v-if="getValue(`tokens[${index}].id`)" v-b-toggle="'collapse-token-' + index" variant="primary">ID: {{ getValue(`tokens[${index}].id`) }}</b-button>
              <b-button v-else v-b-toggle="'collapse-token-' + index" variant="primary">New Token</b-button>

              <b-collapse :id="'collapse-token-' + index" class="mt-2">
                <b-card>
                  <b-col sm="6">
                    <h5>ID: {{ getValue(`tokens[${index}].id`) }}</h5>
                  </b-col><br />

                  <ShowHidePassword
                    :id="`${tokenId}_${index}`"
                    :onBlur="() => setTouched({path: `tokens[${index}].token`, touched: true})"
                    :value="getValue(`tokens[${index}].token`)"
                    :onUpdate="(value) => setValue({ path: `tokens[${index}].token`, value })"
                    :state="showError(`tokens[${index}].token`)"
                    :error="getError(`tokens[${index}].token`)"
                    :generateToken="() => generateToken(`tokens[${index}].token`)"
                  />
<!--                  <b-col sm="6">-->
<!--                    <label :for="`${tokenId}_${index}`">Token:</label>-->
<!--                    <b-form-input-->
<!--                      :id="`${tokenId}_${index}`"-->
<!--                      type="text" @blur="() => setTouched({path: 'tokens[0].token', touched: true})"-->
<!--                      :value="getValue('tokens[0].token')"-->
<!--                      @update="(value) => setValue({ path: 'tokens[0].token', value })"-->
<!--                      :state="showError('tokens[0].token')"-->
<!--                    />-->
<!--                    <b-form-invalid-feedback>{{ getError(`tokens[${index}].token`) }}</b-form-invalid-feedback>-->
<!--                    <b-button variant="outline-primary" @click="addNewContact">-->
<!--                     <b-icon icon="eye" class="ml-2"></b-icon> Add New Contact-->
<!--                    </b-button>-->
<!--                  </b-col>-->
                  <br />

                  <b-col sm="6">
                    <label :for="`${tokenEnabledId}_${index}`">Enabled:</label>
                    <b-form-select
                      :id="`${tokenEnabledId}_${index}`"
                      :value="getValue(`tokens[${index}].enabled`)"
                      :options="[{ item: true, name: 'Enabled' }, { item: false, name: 'Disabled' }]"
                      @change="(value) => setValue({ path: `tokens[${index}].enabled`, value })"
                      @blur.native="() => setTouched({path: `tokens[${index}].enabled`, touched: true})"
                      value-field="item"
                      text-field="name"
                    ></b-form-select>
                  </b-col><br />

                  <b-col sm="6">
                    <label :for="`${tokenNbfId}_${index}`">Not Before Date:</label>
                    <b-container>
                      <b-form-row align-h="start">
                        <b-col>
                          <b-form-input
                            :id="`${tokenNbfId}_${index}`"
                            type="date"
                            @blur="() => setTouched({path: `tokens[${index}].notBefore`, touched: true})"
                            :value="getDate(`tokens[${index}].notBefore`)"
                            @update="(value) => updateDate(value, `tokens[${index}].notBefore`)"
                            :state="showError(`tokens[${index}].notBefore`)"
                          />
                          <b-form-invalid-feedback>{{ getError(`tokens[${index}].notBefore`) }}</b-form-invalid-feedback><br />
                        </b-col>

                        <b-col>
                          <b-form-input
                            type="time"
                            @blur="() => setTouched({path: `tokens[${index}].notBefore`, touched: true})"
                            :value="getTime(`tokens[${index}].notBefore`)"
                            @update="(value) => updateTime(value, `tokens[${index}].notBefore`)"
                            :state="showError(`tokens[${index}].notBefore`)"
                          />
                          <b-form-invalid-feedback>{{ getError(`tokens[${index}].notBefore`) }}</b-form-invalid-feedback><br />
                        </b-col>
                      </b-form-row>
                    </b-container>
                  </b-col>

                  <b-col sm="6">
                      <label :for="`${tokenNafId}_${index}`">Not After Date:</label>
                      <b-container>
                        <b-form-row align-h="start">
                          <b-col>
                            <b-form-input
                              :id="`${tokenNafId}_${index}`"
                              type="date"
                              @blur="() => setTouched({path: `tokens[${index}].notAfter`, touched: true})"
                              :value="getDate(`tokens[${index}].notAfter`)"
                              @update="(value) => updateDate(value, `tokens[${index}].notAfter`)"
                              :state="showError(`tokens[${index}].notAfter`)"
                            />
                            <b-form-invalid-feedback>{{ getError(`tokens[${index}].notAfter`) }}</b-form-invalid-feedback><br />
                          </b-col>

                          <b-col>
                            <b-form-input
                              id="input-tokens-not-after-date"
                              type="time"
                              @blur="() => setTouched({path: `tokens[${index}].notAfter`, touched: true})"
                              :value="getTime(`tokens[${index}].notAfter`)"
                              @update="(value) => updateTime(value, `tokens[${index}].notAfter`)"
                              :state="showError(`tokens[${index}].notAfter`)"
                            />
                            <b-form-invalid-feedback>{{ getError(`tokens[${index}].notAfter`) }}</b-form-invalid-feedback><br />
                          </b-col>
                        </b-form-row>
                      </b-container>
                    </b-col>

                  <b-button variant="outline-danger" @click="deleteSelectedToken(index)">
                    <b-icon icon="trash" class="ml-2"></b-icon> Remove Token
                  </b-button>
                </b-card>
              </b-collapse><br />
            </span><!-- end tokens iterator -->
          </b-card>
        </div>

        <br />

        <div>
          <b-card>
            <h2 class="text-secondary">Contacts:</h2>

            <div>
              <b-button-group size="md">
                <b-button variant="outline-primary" @click="addNewContact">
                  <b-icon icon="person-fill" class="ml-2"></b-icon> Add New Contact
                </b-button>
              </b-button-group>
            </div>

            <span v-for="(contact, index) in getValue('contacts')" :key="'B'+ index">
              <br />
              <b-button v-if="getValue(`contacts[${index}].id`)" v-b-toggle="'collapse-contact-' + index" variant="primary">ID: {{ getValue(`contacts[${index}].id`) }}</b-button>
              <b-button v-else v-b-toggle="'collapse-contact-' + index" variant="primary">New Contact</b-button>

              <b-collapse :id="'collapse-contact-' + index" class="mt-2">
                <b-card>
                  <b-col sm="6">
                    <h5>ID: {{ getValue(`contacts[${index}].id`) }}</h5>
                  </b-col><br />

                  <b-col sm="6">
                    <label :for="`${contactFirstNameId}_${index}`">First Name:</label>
                    <b-form-input
                      :id="`${contactFirstNameId}_${index}`"
                      type="text" @blur="() => setTouched({path: `contacts[${index}].firstName`, touched: true})"
                      :value="getValue(`contacts[${index}].firstName`)"
                      @update="(value) => setValue({ path: `contacts[${index}].firstName`, value })"
                      :state="showError(`contacts[${index}].firstName`)"
                    />
                    <b-form-invalid-feedback>{{ getError(`contacts[${index}].firstName`) }}</b-form-invalid-feedback>
                  </b-col><br />

                  <b-col sm="6">
                    <label :for="`${contactLastNameId}_${index}`">Last Name:</label>
                    <b-form-input
                      :id="`${contactLastNameId}_${index}`"
                      type="text" @blur="() => setTouched({path: `contacts[${index}].lastName`, touched: true})"
                      :value="getValue(`contacts[${index}].lastName`)"
                      @update="(value) => setValue({ path: `contacts[${index}].lastName`, value })"
                      :state="showError(`contacts[${index}].lastName`)"
                    />
                    <b-form-invalid-feedback>{{ getError(`contacts[${index}].lastName`) }}</b-form-invalid-feedback>
                  </b-col><br />

                  <b-col sm="6">
                    <label :for="`${contactEmailId}_${index}`">Email Address:</label>
                    <b-form-input
                      :id="`${contactEmailId}_${index}`"
                      type="text" @blur="() => setTouched({path: `contacts[${index}].emailAddress`, touched: true})"
                      :value="getValue(`contacts[${index}].emailAddress`)"
                      @update="(value) => setValue({ path: `contacts[${index}].emailAddress`, value })"
                      :state="showError(`contacts[${index}].emailAddress`)"
                    />
                    <b-form-invalid-feedback>{{ getError(`contacts[${index}].emailAddress`) }}</b-form-invalid-feedback>
                  </b-col><br />

                  <b-button variant="outline-danger" @click="deleteSelectedContact(index)">
                    <b-icon icon="trash" class="ml-2"></b-icon> Remove Contact
                  </b-button>
                </b-card>
              </b-collapse><br />
            </span><!-- end contacts iterator -->
          </b-card>
        </div>

        <br />
        <div>
          <b-button v-if="showSubmit" type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Save</b-button>
          <b-button v-if="formDirty" type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Reset</b-button>
        </div>
        <hr /><br />
      </b-form>
    </div>
    <div v-else>
      <b-spinner/>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters, mapMutations } from 'vuex';
import genId from '@/components/idGenerator';
import ShowHidePassword from '@/components/ShowHidePassword.vue';

export default {
  components: {
    ShowHidePassword,
  },
  props: ['id'],
  data() {
    return {
      orgNameId: '',
      enabledId: '',
      websiteId: '',
      uIdPrefixId: '',
      tokenEnabledId: '',
      tokenId: '',
      tokenNbfId: '',
      tokenNafId: '',
      contactFirstNameId: '',
      contactLastNameId: '',
      contactEmailId: '',
    };
  },
  beforeMount() {
    this.orgNameId = genId();
    this.enabledId = genId();
    this.websiteId = genId();
    this.uIdPrefixId = genId();
    this.tokenEnabledId = genId();
    this.tokenId = genId();
    this.tokenNbfId = genId();
    this.tokenNafId = genId();
    this.contactFirstNameId = genId();
    this.contactLastNameId = genId();
    this.contactEmailId = genId();
  },
  methods: {
    ...mapMutations('providerForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('provider', ['loadProvider', 'saveProvider']),
    ...mapActions('providerForm', ['submit', 'reset', 'generateToken']),
    updateDate(date, path) {
      const timeValue = this.getTime(path);
      this.setValue({ value: `${date}T${timeValue}.000Z`, path });
    },
    updateTime(time, path) {
      const dateValue = this.getDate(path);
      this.setValue({ path, value: `${dateValue}T${time}:00.000Z` });
    },
    addNewContact() {
      this.addToArray({ path: 'contacts' });
    },
    addNewToken() {
      this.addToArray({ path: 'tokens' });
    },
    deleteSelectedContact(index) {
      this.deleteFromArray(`contacts[${index}]`);
    },
    deleteSelectedToken(index) {
      this.deleteFromArray(`tokens[${index}]`);
    },
    save() {
      this.submit().then((provider) => this.saveProvider({ provider, id: this.id }));
    },
  },

  computed: {
    ...mapGetters('provider', ['fetchedProvider']),
    ...mapGetters('providerForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
    ready() {
      return !this.isEdit || this.fetchedProvider;
    },
    showError() {
      return (path) => ((!this.isTouched(path) && this.getError(path)) ? false : null);
    },
    showSubmit() {
      return this.formDirty && !this.formHasUntouchedErrors;
    },
    getDate() {
      return (path) => {
        const currentDateTime = this.getValue(path);
        return currentDateTime ? currentDateTime.slice(0, 10) : '';
      };
    },
    getTime() {
      return (path) => {
        const currentDateTime = this.getValue(path);
        return currentDateTime ? currentDateTime.slice(11, 19) : '';
      };
    },
    isEdit() {
      return this.id || this.id === 0;
    },
    items() {
      return [
        {
          text: 'Crowbar',
          to: { name: 'Home' },
        },
        {
          text: 'Provider',
          to: { name: 'Provider' },
        },
        {
          text: this.isEdit ? `${this.id}` : 'New Provider',
          active: true,
        },
      ];
    },
  },
  watch: {
    id(oldId, newId) {
      if (newId != null) {
        this.load(newId).then(this.initialize);
      } else {
        this.initialize();
      }
    },
  },
  created() {
    if (this.id != null) {
      this.loadProvider(this.id).then(this.initialize);
    } else {
      this.initialize();
    }
  },

};
</script>
