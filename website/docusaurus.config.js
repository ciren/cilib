module.exports = {
  title: "CIlib",
  tagline: "Reproducible, Pure and Type-sage Evolutionary Computation and Swarm Intelligence",
  url: "https://cilib.net/",
  baseUrl: "/",
  favicon: "img/favicon.ico",
  organizationName: "ciren",
  projectName: "cilib",
  themeConfig: {
    navbar: {
      title: "CIlib",
      logo: {
        alt: 'My Site Logo',
        src: 'img/logo.svg',
      },
      items: [
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
            },
            {
                label: "API",
                to: "https://www.google.com"
            }
          ]
        },
        {
          title: "Community",
            items: [
                {
                    label: "Gitter.im / Matrix",
                    href: "https://gitter.im/cirg-up/cilib"
                }
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
      additionalLanguages: ['java', 'scala', 'protobuf'],
      theme: require('prism-react-renderer/themes/nightOwlLight'),
      darkTheme: require('prism-react-renderer/themes/dracula')
    },
  },
  presets: [
    [
      "@docusaurus/preset-classic",
      {
        docs: {
          //homePageId: 'getting-started',
          path: "../cilib-docs/target/mdoc",
          sidebarPath: require.resolve("./sidebars.js"),
//          routeBasePath: 'docs',
        },
        // theme: {
        //   customCss: require.resolve("./src/css/custom.css")
        // }
      }
    ]
  ]
};
