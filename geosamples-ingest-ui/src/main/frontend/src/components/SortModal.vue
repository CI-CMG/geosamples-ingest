<template>
  <b-form @submit.prevent="saveForm" @reset.prevent="reset">
    <b-container>
      <b-row>
        <b-col>
          <b-list-group>
            <ColumnSelector
              v-for="(row, index) in getValue('available')"
              :key="getValue(`available[${index}].key`)"
              :onAdd="() => onAdd(index)"
              :text="getValue(`available[${index}].column`)"/>
          </b-list-group>
        </b-col>
        <b-col>
          <b-list-group>
            <ColumnOrderer
              v-for="( row, index) in getValue('selected')"
              :key="getValue(`selected[${index}].key`)"
              :onRemove="() => onRemove(index)"
              :text="getValue(`selected[${index}].column`)"
              :onUp="() => onUp(index)"
              :onDown="() => onDown(index)"
              :asc="getValue(`selected[${index}].asc`)"
              :onToggle="() => onToggle(index)"
              :index="index"
              :length="getValue('selected').length"
            />
          </b-list-group>
        </b-col>
      </b-row>
    </b-container>

    <div>
      <b-button v-if="showSubmit" type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Sort</b-button>
      <b-button v-if="formDirty" type="reset" variant="secondary" class="mb-2 mr-sm-2 mb-sm-0">Reset</b-button>
    </div>

  </b-form>
</template>

<script>
import ColumnSelector from './ColumnSelector.vue';
import ColumnOrderer from './ColumnOrderer.vue';

export default {
  components: {
    ColumnSelector,
    ColumnOrderer,
  },
  props: ['hideSort', 'onSort', 'module'],
  computed: {
    getValue() {
      return this.$store.getters[`${this.module}/getValue`];
    },
    formDirty() {
      return this.$store.getters[`${this.module}/formDirty`];
    },
    getError() {
      return this.$store.getters[`${this.module}/getError`];
    },
    isTouched() {
      return this.$store.getters[`${this.module}/isTouched`];
    },
    formHasUntouchedErrors() {
      return this.$store.getters[`${this.module}/formHasUntouchedErrors`];
    },
    showError() {
      return (path) => ((!this.isTouched(path) && this.getError(path)) ? false : null);
    },
    showSubmit() {
      return this.formDirty && !this.formHasUntouchedErrors;
    },
  },
  methods: {
    initialize(value) {
      this.$store.commit(`${this.module}/initialize`, value);
    },
    setValue(value) {
      this.$store.commit(`${this.module}/setValue`, value);
    },
    setTouched(value) {
      this.$store.commit(`${this.module}/setTouched`, value);
    },
    setError(value) {
      this.$store.commit(`${this.module}/setError`, value);
    },
    deleteFromArray(value) {
      this.$store.commit(`${this.module}/deleteFromArray`, value);
    },
    addToArray(value) {
      this.$store.commit(`${this.module}/addToArray`, value);
    },
    submit(value) {
      return this.$store.dispatch(`${this.module}/submit`, value);
    },
    reset(value) {
      return this.$store.dispatch(`${this.module}/reset`, value);
    },
    onAdd(index) {
      const column = this.getValue(`available[${index}].column`);
      const key = this.getValue(`available[${index}].key`);
      this.addToArray({ path: 'selected', value: { column, key, asc: true } });
      this.deleteFromArray(`available[${index}]`);
    },
    onRemove(index) {
      const column = this.getValue(`selected[${index}].column`);
      const key = this.getValue(`selected[${index}].key`);
      this.addToArray({ path: 'available', value: { column, key } });
      this.deleteFromArray(`selected[${index}]`);
    },
    onUp(index) {
      const newArray = [];
      const column = this.getValue(`selected[${index}].column`);
      const key = this.getValue(`selected[${index}].key`);
      const asc = this.getValue(`selected[${index}].asc`);
      const prevColumn = this.getValue(`selected[${index - 1}].column`);
      const prevKey = this.getValue(`selected[${index - 1}].key`);
      const prevAsc = this.getValue(`selected[${index - 1}].asc`);
      const length = this.getValue('selected').length;
      for (let i = 0; i < length; i += 1) {
        if (i === (index - 1)) {
          newArray.push({ column, asc, key });
        } else if (i === index) {
          newArray.push({ column: prevColumn, asc: prevAsc, key: prevKey });
        } else {
          newArray.push({ column: this.getValue(`selected[${i}].column`), asc: this.getValue(`selected[${i}].asc`), key: this.getValue(`selected[${i}].key`) });
        }
      }
      for (let i = 0; i < length; i += 1) {
        this.deleteFromArray('selected[0]');
      }
      for (let i = 0; i < newArray.length; i += 1) {
        this.addToArray({ path: 'selected', value: newArray[i] });
      }
    },
    onDown(index) {
      const newArray = [];
      const column = this.getValue(`selected[${index}].column`);
      const key = this.getValue(`selected[${index}].key`);
      const asc = this.getValue(`selected[${index}].asc`);
      const nextColumn = this.getValue(`selected[${index + 1}].column`);
      const nextKey = this.getValue(`selected[${index + 1}].key`);
      const nextAsc = this.getValue(`selected[${index + 1}].asc`);
      const length = this.getValue('selected').length;
      for (let i = 0; i < length; i += 1) {
        if (i === index) {
          newArray.push({ column: nextColumn, asc: nextAsc, key: nextKey });
        } else if (i === index + 1) {
          newArray.push({ column, asc, key });
        } else {
          newArray.push({ column: this.getValue(`selected[${i}].column`), asc: this.getValue(`selected[${i}].asc`), key: this.getValue(`selected[${i}].key`) });
        }
      }
      for (let i = 0; i < length; i += 1) {
        this.deleteFromArray('selected[0]');
      }
      for (let i = 0; i < newArray.length; i += 1) {
        this.addToArray({ path: 'selected', value: newArray[i] });
      }
    },
    onToggle(index) {
      const value = this.getValue(`selected[${index}].asc`);
      this.setValue({ path: `selected[${index}].asc`, value: value === 'true' ? 'false' : 'true' });
    },
    saveForm() {
      this.submit()
        .then((sortParameters) => {
          this.hideSort();
          this.onSort(sortParameters);
        });
    },
  },
};
</script>
