"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[331],{3905:function(e,t,n){n.d(t,{Zo:function(){return s},kt:function(){return f}});var r=n(7294);function i(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function a(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function o(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?a(Object(n),!0).forEach((function(t){i(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):a(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function l(e,t){if(null==e)return{};var n,r,i=function(e,t){if(null==e)return{};var n,r,i={},a=Object.keys(e);for(r=0;r<a.length;r++)n=a[r],t.indexOf(n)>=0||(i[n]=e[n]);return i}(e,t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);for(r=0;r<a.length;r++)n=a[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(i[n]=e[n])}return i}var c=r.createContext({}),u=function(e){var t=r.useContext(c),n=t;return e&&(n="function"==typeof e?e(t):o(o({},t),e)),n},s=function(e){var t=u(e.components);return r.createElement(c.Provider,{value:t},e.children)},p={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},d=r.forwardRef((function(e,t){var n=e.components,i=e.mdxType,a=e.originalType,c=e.parentName,s=l(e,["components","mdxType","originalType","parentName"]),d=u(n),f=i,m=d["".concat(c,".").concat(f)]||d[f]||p[f]||a;return n?r.createElement(m,o(o({ref:t},s),{},{components:n})):r.createElement(m,o({ref:t},s))}));function f(e,t){var n=arguments,i=t&&t.mdxType;if("string"==typeof e||i){var a=n.length,o=new Array(a);o[0]=d;var l={};for(var c in t)hasOwnProperty.call(t,c)&&(l[c]=t[c]);l.originalType=e,l.mdxType="string"==typeof e?e:i,o[1]=l;for(var u=2;u<a;u++)o[u]=n[u];return r.createElement.apply(null,o)}return r.createElement.apply(null,n)}d.displayName="MDXCreateElement"},345:function(e,t,n){n.r(t),n.d(t,{assets:function(){return s},contentTitle:function(){return c},default:function(){return f},frontMatter:function(){return l},metadata:function(){return u},toc:function(){return p}});var r=n(7462),i=n(3366),a=(n(7294),n(3905)),o=["components"],l={id:"installation",title:"Installation",hide_title:!0},c="Installation",u={unversionedId:"introduction/installation",id:"introduction/installation",title:"Installation",description:"To include CIlib into your project, add the appropriate library dependencies to your project build tool (adjusting the artifact name for the different available modules).",source:"@site/../cilib-docs/target/mdoc/introduction/installation.md",sourceDirName:"introduction",slug:"/introduction/installation",permalink:"/docs/introduction/installation",draft:!1,tags:[],version:"current",frontMatter:{id:"installation",title:"Installation",hide_title:!0},sidebar:"docs",previous:{title:"Getting Started with CIlib",permalink:"/docs/introduction/getting-started"},next:{title:"Motivation",permalink:"/docs/introduction/motivation"}},s={},p=[],d={toc:p};function f(e){var t=e.components,n=(0,i.Z)(e,o);return(0,a.kt)("wrapper",(0,r.Z)({},d,n,{components:t,mdxType:"MDXLayout"}),(0,a.kt)("h1",{id:"installation"},"Installation"),(0,a.kt)("p",null,"To include CIlib into your project, add the appropriate library dependencies to your project build tool (adjusting the artifact name for the different available modules)."),(0,a.kt)("p",null,"An example, using SBT, would be:"),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-scala"},'libraryDependencies += "net.cilib" %% "cilib-<module>" % "2.0.0+124-2cd7fc13-SNAPSHOT"\n')),(0,a.kt)("p",null,"In the above, the ",(0,a.kt)("inlineCode",{parentName:"p"},"<module>")," identifier can be replaced with one of the following:"),(0,a.kt)("ul",null,(0,a.kt)("li",{parentName:"ul"},(0,a.kt)("inlineCode",{parentName:"li"},"core")," - Contains type class definitions together with required data structures."),(0,a.kt)("li",{parentName:"ul"},(0,a.kt)("inlineCode",{parentName:"li"},"exec")," - Simplistic execution code allowing for experimental execution."),(0,a.kt)("li",{parentName:"ul"},(0,a.kt)("inlineCode",{parentName:"li"},"de")," - Data structures and logic related to Differential Evolution."),(0,a.kt)("li",{parentName:"ul"},(0,a.kt)("inlineCode",{parentName:"li"},"docs")," - Sources for the website."),(0,a.kt)("li",{parentName:"ul"},(0,a.kt)("inlineCode",{parentName:"li"},"ga")," - Data structures and logic related to Genetic Algorithms."),(0,a.kt)("li",{parentName:"ul"},(0,a.kt)("inlineCode",{parentName:"li"},"pso")," - Data structures and logic related to Particle Swarm Optimization."),(0,a.kt)("li",{parentName:"ul"},(0,a.kt)("inlineCode",{parentName:"li"},"moo")," - Type classes, instances and data structures for Multi-Objective Optimization.")),(0,a.kt)("p",null,"Have a look at ",(0,a.kt)("a",{parentName:"p",href:"https://search.maven.org/search?q=cilib"},"the central repository")," for the different import statements for a variety of build tools.\nFor example, ",(0,a.kt)("a",{parentName:"p",href:"https://search.maven.org/artifact/net.cilib/cilib-core_2.12/2.0.0+124-2cd7fc13-SNAPSHOT/jar"},"this")," is the page for the ",(0,a.kt)("inlineCode",{parentName:"p"},"core")," cilib module which lists the different build tool configurations.\nIf there are any specifics that are not answered by these configurations, please feel free to get in contact with us."))}f.isMDXComponent=!0}}]);