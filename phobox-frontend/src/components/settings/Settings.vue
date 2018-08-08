<template>
  <div id="settings_panel">
    <h1>{{ Locale.values.menu.settings }}</h1>
    <security-access></security-access>
    <import-format></import-format>
    <create-thumbnails></create-thumbnails>
    <change-storage></change-storage>
  </div>
</template>

<script>
import Locale from '@/Locale';
import ChangeStorage from '@/components/settings/ChangeStorage';
import CreateThumbnails from '@/components/settings/CreateThumbnails';
import ImportFormat from '@/components/settings/ImportFormat';
import SecurityAccess from '@/components/settings/SecurityAccess';
import ComService from '@/utils/ComService';
Locale.init()

export default {
  name: "Settings",
  components: {
    SecurityAccess,
    ImportFormat,
    CreateThumbnails,
    ChangeStorage
  },
  data() {
    return {
      Locale: Locale
    };
  },
  computed: {},
  methods: {}
};
</script>

<style>
#settings_panel {
  max-width: 700px;
  margin: 0 auto;
  margin-top: -20px;
  padding: 0px 10px;

}

#settings_panel .panel {
  background-color: #191919;
  border-color: #191919;
  margin-bottom: 20px;
  border-radius: 4px;
}

#settings_panel .panel-default > .panel-heading {
  color: #fff;
  background-color: #131111;
  border-color: #000;
  border-radius: 4px 4px 0px 0px;
  font-weight: bold;
  padding: 10px;
  font-size: 1.2em;
}

#settings_panel .alert-success {
  color: #f2fdf3;
  background-color: #7ca06d;
  border-color: #54654d;
}

#settings_panel .panel-body {
  padding: 10px;
}

#settings_panel .alert-danger {
  color: #f1fdf2;
  background-color: #9c5959;
  border-color: #5d2c2c;
}
</style>
