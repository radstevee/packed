import { defineConfig } from "vitepress";

// https://vitepress.dev/reference/site-config
export default defineConfig({
  title: "Packed Documentation",
  description: "A minecraft resource pack compiler library for Kotlin",
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: "Home", link: "/" },
      { text: "Getting started", link: "/getting-started" },
    ],

    sidebar: [
      {
        text: "Documentation",
        items: [
          { text: "Introduction", link: "/introduction" },
          { text: "Getting Started", link: "/getting-started" },
          { text: "Working with Fonts & Font Providers", link: "/fonts" },
          { text: "Assets", link: "/assets" },
          { text: "Plugins", link: "/plugins" },
        ],
      },
    ],

    socialLinks: [
      { icon: "github", link: "https://github.com/radstevee/packed" },
    ],
  },
});
