<template>
  <b-container fluid class="csb-ex-row border rounded pt-2 pb-2">
    <b-row class="csb-ex-row-header-row">
      <b-col class="csb-ex-row-header-row-content d-flex justify-content-between align-items-center">
        <span class="d-flex align-items-center">
          <b-icon class="csb-ex-row-collapse-btn mr-2" :icon="visible ? 'chevron-double-up' : 'chevron-double-down'" @click="() => onCollapseToggle(visible, row)"/>
          <span><slot name="header-left">{{ row.organizationName }}</slot></span>
        </span>
        <span class="d-flex align-items-center"><slot name="header-right"></slot></span>
      </b-col>
    </b-row>
    <b-row>
      <b-col class="">
        <b-collapse :visible="visible" class="border rounded ml-4 mr-2 mt-2 pt-2 pb-2 pr-2 pl-3 bg-light">
          <slot name="details">
            <ExpandableRowDetail :row="row" :fields="fields"/>
          </slot>
        </b-collapse>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
import ExpandableRowDetail from '@/components/ExpandableRowDetail.vue';

export default {
  props: ['row', 'fields'],
  components: {
    ExpandableRowDetail,
  },
  data() {
    return {
      visible: false,
    };
  },
  methods: {
    onCollapseToggle() {
      this.visible = !this.visible;
    },
  },
};
</script>
