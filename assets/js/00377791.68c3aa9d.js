"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[122],{3905:function(e,t,n){n.d(t,{Zo:function(){return u},kt:function(){return h}});var r=n(7294);function i(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function o(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function a(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?o(Object(n),!0).forEach((function(t){i(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):o(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function s(e,t){if(null==e)return{};var n,r,i=function(e,t){if(null==e)return{};var n,r,i={},o=Object.keys(e);for(r=0;r<o.length;r++)n=o[r],t.indexOf(n)>=0||(i[n]=e[n]);return i}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(r=0;r<o.length;r++)n=o[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(i[n]=e[n])}return i}var l=r.createContext({}),c=function(e){var t=r.useContext(l),n=t;return e&&(n="function"==typeof e?e(t):a(a({},t),e)),n},u=function(e){var t=c(e.components);return r.createElement(l.Provider,{value:t},e.children)},p={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},d=r.forwardRef((function(e,t){var n=e.components,i=e.mdxType,o=e.originalType,l=e.parentName,u=s(e,["components","mdxType","originalType","parentName"]),d=c(n),h=i,m=d["".concat(l,".").concat(h)]||d[h]||p[h]||o;return n?r.createElement(m,a(a({ref:t},u),{},{components:n})):r.createElement(m,a({ref:t},u))}));function h(e,t){var n=arguments,i=t&&t.mdxType;if("string"==typeof e||i){var o=n.length,a=new Array(o);a[0]=d;var s={};for(var l in t)hasOwnProperty.call(t,l)&&(s[l]=t[l]);s.originalType=e,s.mdxType="string"==typeof e?e:i,a[1]=s;for(var c=2;c<o;c++)a[c]=n[c];return r.createElement.apply(null,a)}return r.createElement.apply(null,n)}d.displayName="MDXCreateElement"},4899:function(e,t,n){n.r(t),n.d(t,{assets:function(){return u},contentTitle:function(){return l},default:function(){return h},frontMatter:function(){return s},metadata:function(){return c},toc:function(){return p}});var r=n(7462),i=n(3366),o=(n(7294),n(3905)),a=["components"],s={id:"three-requirements",title:"Pre-requisites",hide_title:!0},l="Pre-requisites",c={unversionedId:"introduction/three-requirements",id:"introduction/three-requirements",title:"Pre-requisites",description:"The experiences learned from the first version highlighted the following requirements:",source:"@site/../cilib-docs/target/mdoc/introduction/three-requirements.md",sourceDirName:"introduction",slug:"/introduction/three-requirements",permalink:"/docs/introduction/three-requirements",draft:!1,tags:[],version:"current",frontMatter:{id:"three-requirements",title:"Pre-requisites",hide_title:!0},sidebar:"docs",previous:{title:"Motivation",permalink:"/docs/introduction/motivation"},next:{title:"Related Work",permalink:"/docs/introduction/related-work"}},u={},p=[{value:"Correctness",id:"correctness",level:2},{value:"Type-safety",id:"type-safety",level:2},{value:"Reproducibility",id:"reproducibility",level:2}],d={toc:p};function h(e){var t=e.components,n=(0,i.Z)(e,a);return(0,o.kt)("wrapper",(0,r.Z)({},d,n,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("h1",{id:"pre-requisites"},"Pre-requisites"),(0,o.kt)("p",null,"The experiences learned from the first version highlighted the following requirements:"),(0,o.kt)("h2",{id:"correctness"},"Correctness"),(0,o.kt)("p",null,"Having algorithms be available within an open source project prevents accidental mistakes from going unnoticed.\nCode may be reviewed at any time, allowing for open discussion and corrections to mistakes.\nThe most important property is that an algorithm be correctly implemented.\nWhen any uncertainty presents itself regarding algorithm implementation, the correctness of the implementation will always be preferred over any improvement to the speed of algorithm execution, etc.\nOther improvements may be incorporated once the correctness has been established and is verifiable."),(0,o.kt)("p",null,"As an additional impact to the need for correctness, data should be immutable.\nEnforcing immutable data will also prevent external functions and methods from altering data in error."),(0,o.kt)("h2",{id:"type-safety"},"Type-safety"),(0,o.kt)("p",null,"The Java implementation and the influence of the ",(0,o.kt)("inlineCode",{parentName:"p"},"simulator")," program resulted in an execution structure that relied heavily on reflection and type casting.\nFurthermore, the casting became a neccessary evil when working with deep inheritance hierarchies.\nDeep inheritance results in parent classes and interfaces losing information becuase they are too general.\nAn example of such information loss is present in the collections hierarchy of Java where data structures extend the ",(0,o.kt)("inlineCode",{parentName:"p"},"Collection")," interface.\nIn some cases, the functionality defined in ",(0,o.kt)("inlineCode",{parentName:"p"},"Collection")," is not applicable to a data structure, resulting in oddly behaving methods."),(0,o.kt)("p",null,"Other examples do exist, but the point has been made.\nAt the end of the day, such code is fragile.\n",(0,o.kt)("strong",{parentName:"p"},"Very fragile.")),(0,o.kt)("p",null,"Instead, the type system of the language should be exploited as much as possible.\nUsing the type system, truly generic code may be written where only unneccessary information for the current operation would be ignored.\nThe type system can also ensure that invalid program states are simply not possible to construct, reducing possible defects even further.\nGiven that the type system can provide such safety, no intermediate representation (such as the XML based specification) would be needed."),(0,o.kt)("h2",{id:"reproducibility"},"Reproducibility"),(0,o.kt)("p",null,"The abiltity to reproduce the results of a published work is a fundamental part of the scientific method.\nWith effects such as randomness applied to algorithms, reproducing results becomes much more difficult.\nThe definition of algorithms should allow for this property, effectively allowing an algorithm to be a deterministic process.\nThis would allow for explicit testing of algorithms, without the need of attempts to verify the results using a sample of execution results and statistics."))}h.isMDXComponent=!0}}]);