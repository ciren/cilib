module.exports = {
  title: "CIlib",
  tagline: "Predictable and Pure Evolutionary and Swarm Intelligence",
  url: "https://cilib.net",
  baseUrl: "/",
  favicon: "img/favicon.ico",
  organizationName: "ciren", // Usually your GitHub org/user name.
  projectName: "cilib", // Usually your repo name.
  themeConfig: {
    navbar: {
      title: "CIlib",
      // logo: {
      //   alt: 'My Site Logo',
      //   src: 'img/logo.svg',
      // },
      links: [
        {
          to: "docs/introduction/getting-started",
          label: "Docs",
          position: "right"
        },
        //{to: 'blog', label: 'Blog', position: 'right'},
        {
          href: "https://github.com/ciren/cilib",
          label: "GitHub",
          position: "right"
        }
      ]
    },
    footer: {
      style: "dark",
      links: [
        {
          title: "Docs",
          items: [
            {
              label: "Docs",
              to: "docs/introduction/getting-started"
            }
          ]
        },
        {
          title: "Community",
          items: [
            // {
            //   label: 'Discord',
            //   href: 'https://discordapp.com/invite/docusaurus',
            // },
          ]
        },
        {
          title: "Social",
          items: [
            // {
            //   label: 'Blog',
            //   to: 'blog',
            // },
          ]
        }
      ],
      // logo: {
      //   alt: '',
      //   src: '',
      //   href: '',
      // },
      copyright: `Copyright © ${new Date().getFullYear()} CIlib authors`
    },
    prism: {
      defaultLanguage: "scala"
    }
  },
  presets: [
    [
      "@docusaurus/preset-classic",
      {
        docs: {
          sidebarPath: require.resolve("./sidebars.js")
        },
        theme: {
          customCss: require.resolve("./src/css/custom.css")
        }
      }
    ]
  ]
};
