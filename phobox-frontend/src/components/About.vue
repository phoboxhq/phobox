<template>
  <div id="about">
		<div class="about_panel">
			<h1>{{ $t('about.head') }}</h1>
			<h3>{{ $t('about.background_head') }}</h3>
			<div v-html="$t('about.background_text')"></div>
		
			<h3>{{ $t('about.author_and_contact_head') }}</h3>
			<div v-html="$t('about.author_and_contact_text')"></div>
		
      <h3>{{ $t('about.thirdparty_libs') }}</h3>
      <div class="libs">
        <div class="libItem" v-for="(item, index) in thirdpartyDependencies" :key="index">
          <a :href="item.link"><span class="title">{{ item.title }}</span></a>
          <a :href="item.licenceLink"><span class="licence">{{ item.licence }}</span></a>
        </div>

        <a href="https://github.com/phoboxhq/phobox/network/dependencies">More details here</a>
      </div>
    
    </div>
	</div>
</template>

<script>
export default {
  name: "About",
  data() {
    return {
      thirdpartyDependencies: [
        // Back-end deps
        {title: "metadata-extractor", link: "https://drewnoakes.com/code/exif/", licence: "The Apache Software License, Version 2.0", licenceLink: "http://www.apache.org/licenses/LICENSE-2.0.txt"},
        {title: "jackson-dataformats-text", link: "https://github.com/FasterXML/jackson-dataformats-text", licence: "The Apache Software License, Version 2.0", licenceLink: "http://www.apache.org/licenses/LICENSE-2.0.txt"},
        {title: "h2database", link: "http://www.h2database.com/", licence: "MPL 2.0 or EPL 1.0", licenceLink: "http://h2database.com/html/license.html"},
        {title: "java-image-scaling", link: "http://code.google.com/p/java-image-scaling", licence: "The BSD 3-Clause License", licenceLink: "http://opensource.org/licenses/BSD-3-Clause"},
        {title: "com.sun.xml.bind:jaxb-core", link: "http://jaxb.java.net/jaxb-bundles/jaxb-core", licence: "CDDL+GPL License", licenceLink: "http://glassfish.java.net/public/CDDL+GPL_1_1.html"},
        {title: "jaxb-impl", link: "http://jaxb.java.net/jaxb-bundles/jaxb-impl", licence: "CDDL+GPL License", licenceLink: "http://glassfish.java.net/public/CDDL+GPL_1_1.html"},
        {title: "commons-cli", link: "http://commons.apache.org/proper/commons-cli/", licence: "Apache License, Version 2.0", licenceLink: "http://www.apache.org/licenses/LICENSE-2.0.txt"},
        {title: "commons-io", link: "http://commons.apache.org/io/", licence: "Apache License, Version 2.0", licenceLink: "http://www.apache.org/licenses/LICENSE-2.0.txt"},
        {title: "javamail-android-forked", link: "http://java.sun.com/javase/technologies/desktop/javabeans/jaf/index.jsp", licence: "CDDL 1.0", licenceLink: "https://glassfish.dev.java.net/public/CDDLv1.0.html"},
        {title: "jaxb-spec", link: "http://jaxb.java.net/", licence: "CDDL 1.1", licenceLink: "https://glassfish.java.net/public/CDDL+GPL_1_1.html"},
        {title: "flyway", link: "https://flywaydb.org/flyway-core", licence: "Apache License, Version 2.0", licenceLink: "http://www.apache.org/licenses/LICENSE-2.0.txt"},
        {title: "imgscalr", link: "http://www.thebuzzmedia.com/software/imgscalr-java-image-scaling-library/", licence: "Apache License, Version 2.0", licenceLink: "http://www.apache.org/licenses/LICENSE-2.0.txt"},
        {title: "org.openjfx:javafx-controls", link: "http://openjdk.java.net/projects/openjfx/javafx-controls/", licence: "GPLv2+CE", licenceLink: "http://openjdk.java.net/legal/gplv2+ce.html"},
        {title: "org.openjfx:javafx-fxml", link: "http://openjdk.java.net/projects/openjfx/javafx-fxml/", licence: "GPLv2+CE", licenceLink: "http://openjdk.java.net/legal/gplv2+ce.html"},
        {title: "org.openjfx:javafx-media", link: "http://openjdk.java.net/projects/openjfx/javafx-media/", licence: "GPLv2+CE", licenceLink: "http://openjdk.java.net/legal/gplv2+ce.html"},
        {title: "org.openjfx:javafx-swing", link: "http://openjdk.java.net/projects/openjfx/javafx-swing/", licence: "GPLv2+CE", licenceLink: "http://openjdk.java.net/legal/gplv2+ce.html"},
        {title: "jrawio-src", link: "http://jrawio.rawdarkroom.org/it.tidalwave.imageio.raw", licence: "Apache License, Version 2.0", licenceLink: "http://www.apache.org/licenses/LICENSE-2.0.txt"},
        {title: "spring-boot", link: "https://projects.spring.io/spring-boot/", licence: "Apache License, Version 2.0", licenceLink: "http://www.apache.org/licenses/LICENSE-2.0.txt"},
        // Front-end deps
        {title: "elevatezoom", link: "https://github.com/zeitiger/elevatezoom#readme", licence: "MIT", licenceLink: "https://opensource.org/licenses/mit-license.php"},
        {title: "bootstrap-css-only", link: "http://getbootstrap.com/", licence: "MIT", licenceLink: "https://opensource.org/licenses/mit-license.php"},
        {title: "bootstrap-tagsinput", link: "http://timschlechter.github.io/bootstrap-tagsinput/examples/", licence: "MIT", licenceLink: "https://opensource.org/licenses/mit-license.php"},
        {title: "chart.js", link: "https://www.chartjs.org/", licence: "MIT", licenceLink: "https://opensource.org/licenses/mit-license.php"},
        {title: "dropzone", link: "http://www.dropzonejs.com/", licence: "MIT", licenceLink: "https://opensource.org/licenses/mit-license.php"},
        {title: "font-awesome", link: "http://fontawesome.io/", licence: "MIT", licenceLink: "https://opensource.org/licenses/mit-license.php"},
        {title: "jquery", link: "https://jquery.com/", licence: "MIT", licenceLink: "https://opensource.org/licenses/mit-license.php"},
        {title: "lato-webfont", link: "http://www.latofonts.com/", licence: "MIT", licenceLink: "https://opensource.org/licenses/mit-license.php"},
        {title: "material-design-icons-iconfont", link: "https://jossef.github.io/material-design-icons-iconfont/", licence: "Apache-2.0", licenceLink: "http://www.apache.org/licenses/LICENSE-2.0.txt"},
        {title: "vue", link: "https://github.com/vuejs/vue#readme", licence: "MIT", licenceLink: "https://opensource.org/licenses/mit-license.php"},
        {title: "vue-chartjs", link: "http://vue-chartjs.org/", licence: "MIT", licenceLink: "https://opensource.org/licenses/mit-license.php"},
        {title: "vue-i18n", link: "https://github.com/kazupon/vue-i18n#readme", licence: "MIT", licenceLink: "https://opensource.org/licenses/mit-license.php"},
        {title: "vue-product-zoomer", link: "https://github.com/akulubala/vue-product-zoomer#readme", licence: "MIT", licenceLink: "https://opensource.org/licenses/mit-license.php"},
        {title: "vue-resource", link: "https://github.com/pagekit/vue-resource#readme", licence: "MIT", licenceLink: "https://opensource.org/licenses/mit-license.php"},
        {title: "vue-router", link: "https://github.com/vuejs/vue-router#readme", licence: "MIT", licenceLink: "https://opensource.org/licenses/mit-license.php"},
        {title: "vue2-dropzone", link: "https://github.com/rowanwins/vue-dropzone#readme", licence: "MIT", licenceLink: "https://opensource.org/licenses/mit-license.php"},
      ]
    };
  }
};
</script>

<style>
#about {
  margin: 0 auto;
  max-width: 700px;
}

.about_panel {
  margin: -20px 10px;
  padding: 10px;
  border: 1px solid #0e0d0d;
  border-radius: 6px;
  background: #191717;
}

.about_panel p {
  text-align: justify;
}

.libItem {
  display: grid;
  grid-template-columns: 40% 60%;
}
</style>
