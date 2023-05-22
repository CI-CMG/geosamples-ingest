<template>
  <div>
    <b-table
      sticky-header="500px"
      head-variant="dark"
      striped
      bordered
      small
      hover
      :items="items"
      :fields="fields">
      <template #head()="data">
      <span :style="{
        width: '100%',
      }">
        <span>{{ data.label }}</span>
        <b-icon-caret-up-fill class="ml-2" v-if="sorted(data.column).sorted === 'asc'"></b-icon-caret-up-fill>
        <b-icon-caret-down-fill class="ml-2" v-if="sorted(data.column).sorted === 'desc'"></b-icon-caret-down-fill>
        <span v-if="sorted(data.column).index >= 0">{{ sorted(data.column).index }}</span>
      </span>

      </template>
      <template #cell(approvalState)="data">
        <b-link @click="showApproval(data.item)">{{ data.item.approvalState }}</b-link>
      </template>
      <template #cell(selected)="data">
        <b-form-checkbox plain :checked="data.item.selected" @change="() => toggleSelect(data.index)"/>
      </template>
      <template #cell(publish)="data">
        <b-form-checkbox :disabled="true" plain :checked="data.item.publish"/>
      </template>
      <template #cell(imlgs)="data">
        <b-link :to="{ name: 'SampleEdit', params: { id: data.item.imlgs }}">{{ data.item.imlgs }}</b-link>
      </template>
      <template #cell(interval)="data">
        <b-link :to="{ name: 'IntervalEdit', params: { imlgs: data.item.imlgs, id: data.item.intervalId }}">{{ data.item.interval }}</b-link>
      </template>
      <template #cell(ages)="data">
        <span v-if="data.item.ages">{{ data.item.ages.join(', ') }}</span>
      </template>
    </b-table>
    <b-modal ref="approval" title="Review Sample">
      <b-form-group label="Approval State">
        <b-form-select v-model="approvalState" :options="[
              { text: 'APPROVED', value: 'APPROVED' },
              { text: 'REJECTED', value: 'REJECTED' },
              { text: 'PENDING', value: 'PENDING' },
            ]"
                       :state="showError('approvalState')"
        />
        <b-form-invalid-feedback>{{ $store.getters['approvalForm/getError']('approvalState') }}</b-form-invalid-feedback>
      </b-form-group>
      <b-form-group label="Comment">
        <b-form-textarea v-model="comment" :state="showError('comment')"/>
        <b-form-invalid-feedback>{{ $store.getters['approvalForm/getError']('comment') }}</b-form-invalid-feedback>
      </b-form-group>
      <template #modal-footer>
        <b-button v-if="showSubmit" variant="primary" @click="saveApproval">
          <b-icon icon="check" class="mr-2"/>Submit
        </b-button>
        <b-button variant="secondary" @click="hideApproval">
          <b-icon icon="x" class="mr-2"/>Close
        </b-button>
        <b-button  v-if="$store.getters['approvalForm/formDirty']" variant="danger" @click="resetApproval">
          <b-icon icon="arrow-counterclockwise" class="mr-2"/>Reset
        </b-button>
      </template>
    </b-modal>
  </div>
</template>

<script>
export default {
  props: [
    'sortChanged',
    'sortedColumns',
    'sortDesc',
    'items',
    'fields',
    'toggleSelect',
  ],
  computed: {
    indexes() {
      const indexMap = {};
      this.sortedColumns.forEach(({ key }, index) => {
        indexMap[key] = index;
      });
      return indexMap;
    },
    sorted() {
      return (key) => {
        if (!this.sortedColumns) {
          return { sorted: '', index: -1 };
        }
        const index = this.indexes[key];
        if (index != null) {
          const selected = this.sortedColumns[index];
          if (selected.asc) {
            return { sorted: 'asc', index };
          }
          return { sorted: 'desc', index };
        }
        return { sorted: '', index: -1 };
      };
    },
    approvalState: {
      get() {
        return this.$store.getters['approvalForm/getValue']('approvalState');
      },
      set(value) {
        this.$store.commit('approvalForm/setValue', { path: 'approvalState', value });
      },
    },

    comment: {
      get() {
        return this.$store.getters['approvalForm/getValue']('comment');
      },
      set(value) {
        this.$store.commit('approvalForm/setValue', { path: 'comment', value });
      },
    },

    showError() {
      return (path) => ((!this.$store.getters['approvalForm/isTouched'](path) && this.$store.getters['approvalForm/getError'](path)) ? false : null);
    },
    showSubmit() {
      return this.$store.getters['approvalForm/formDirty'] && !this.$store.getters['approvalForm/formHasUntouchedErrors'];
    },
  },
  methods: {
    showApproval(item) {
      this.currentItem = item;
      this.$refs.approval.show();
      this.$store.dispatch('interval/loadApproval', item.intervalId).then(
        (data) => {
          this.$store.commit('approvalForm/initialize', {
            approvalState: data.approvalState,
            comment: data.comment,
          });
        },
      );
    },

    hideApproval() {
      this.$refs.approval.hide();
      this.currentItem = null;
    },

    saveApproval() {
      this.$store.dispatch('approvalForm/submit').then(
        (approval) => {
          this.$store.dispatch('interval/saveApproval', { id: this.currentItem.intervalId, approval }).then(
            () => {
              this.hideApproval();
              this.$store.dispatch('interval/searchPage');
            },
          );
        },
      );
    },

    resetApproval() {
      this.$store.commit('approvalForm/reset');
    },
  },
};
</script>
