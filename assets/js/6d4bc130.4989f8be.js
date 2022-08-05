"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[339],{3905:function(e,n,t){t.d(n,{Zo:function(){return p},kt:function(){return d}});var r=t(7294);function a(e,n,t){return n in e?Object.defineProperty(e,n,{value:t,enumerable:!0,configurable:!0,writable:!0}):e[n]=t,e}function o(e,n){var t=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);n&&(r=r.filter((function(n){return Object.getOwnPropertyDescriptor(e,n).enumerable}))),t.push.apply(t,r)}return t}function i(e){for(var n=1;n<arguments.length;n++){var t=null!=arguments[n]?arguments[n]:{};n%2?o(Object(t),!0).forEach((function(n){a(e,n,t[n])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(t)):o(Object(t)).forEach((function(n){Object.defineProperty(e,n,Object.getOwnPropertyDescriptor(t,n))}))}return e}function s(e,n){if(null==e)return{};var t,r,a=function(e,n){if(null==e)return{};var t,r,a={},o=Object.keys(e);for(r=0;r<o.length;r++)t=o[r],n.indexOf(t)>=0||(a[t]=e[t]);return a}(e,n);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(r=0;r<o.length;r++)t=o[r],n.indexOf(t)>=0||Object.prototype.propertyIsEnumerable.call(e,t)&&(a[t]=e[t])}return a}var l=r.createContext({}),u=function(e){var n=r.useContext(l),t=n;return e&&(t="function"==typeof e?e(n):i(i({},n),e)),t},p=function(e){var n=u(e.components);return r.createElement(l.Provider,{value:n},e.children)},m={inlineCode:"code",wrapper:function(e){var n=e.children;return r.createElement(r.Fragment,{},n)}},c=r.forwardRef((function(e,n){var t=e.components,a=e.mdxType,o=e.originalType,l=e.parentName,p=s(e,["components","mdxType","originalType","parentName"]),c=u(t),d=a,f=c["".concat(l,".").concat(d)]||c[d]||m[d]||o;return t?r.createElement(f,i(i({ref:n},p),{},{components:t})):r.createElement(f,i({ref:n},p))}));function d(e,n){var t=arguments,a=n&&n.mdxType;if("string"==typeof e||a){var o=t.length,i=new Array(o);i[0]=c;var s={};for(var l in n)hasOwnProperty.call(n,l)&&(s[l]=n[l]);s.originalType=e,s.mdxType="string"==typeof e?e:a,i[1]=s;for(var u=2;u<o;u++)i[u]=t[u];return r.createElement.apply(null,i)}return r.createElement.apply(null,t)}c.displayName="MDXCreateElement"},2276:function(e,n,t){t.r(n),t.d(n,{assets:function(){return p},contentTitle:function(){return l},default:function(){return d},frontMatter:function(){return s},metadata:function(){return u},toc:function(){return m}});var r=t(7462),a=t(3366),o=(t(7294),t(3905)),i=["components"],s={},l=void 0,u={unversionedId:"design/rvar",id:"design/rvar",title:"rvar",description:"An instance of RVar represents a computation that, when executed,",source:"@site/../cilib-docs/target/mdoc/design/rvar.md",sourceDirName:"design",slug:"/design/rvar",permalink:"/docs/design/rvar",draft:!1,tags:[],version:"current",frontMatter:{}},p={},m=[],c={toc:m};function d(e){var n=e.components,t=(0,a.Z)(e,i);return(0,o.kt)("wrapper",(0,r.Z)({},c,t,{components:n,mdxType:"MDXLayout"}),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-scala"},"\nmdoc:invisible\nimport cilib._\n\nimport eu.timepit.refined.api.Refined\nimport eu.timepit.refined.numeric.Positive\nimport eu.timepit.refined.auto._\n\nimport zio.prelude._\n")),(0,o.kt)("h1",{id:"rvar"},"RVar"),(0,o.kt)("p",null,"An instance of ",(0,o.kt)("inlineCode",{parentName:"p"},"RVar")," represents a computation that, when executed,\nresults in a value with randomness applied. ",(0,o.kt)("inlineCode",{parentName:"p"},"RVar")," is one of the most\nimportant structures in CIlib and is therefore discussed first in order to\nunderstand how the data structure works."),(0,o.kt)("p",null,(0,o.kt)("inlineCode",{parentName:"p"},"RVar")," has a monad instance and therefore allows a for a large amount\nof composition, but more importantly allows for the tracking of randomness\nwithin the ",(0,o.kt)("inlineCode",{parentName:"p"},"RVar")," computation. This tracking is of the utmost importance\nwithin computational intelligence algorithms, as randomness needs to be\ncontrolled in a manner that facilitates repetition. In other words,\neven if a computation uses randomness, given the same inputs, the same\nresults are expected even with randomness applied."),(0,o.kt)("p",null,"Due to the monadic nature of the data structure, the data structure may be\ntransformed by functions such as ",(0,o.kt)("inlineCode",{parentName:"p"},"map"),", ",(0,o.kt)("inlineCode",{parentName:"p"},"flatMap"),", etc"),(0,o.kt)("p",null,"There are several predefined combinators that allow the user to use and\ncreate ",(0,o.kt)("inlineCode",{parentName:"p"},"RVar")," computations. These include functions for randomness applied\nto primitive types (such and ",(0,o.kt)("inlineCode",{parentName:"p"},"Int")," and ",(0,o.kt)("inlineCode",{parentName:"p"},"Double"),") to more complex types that\nbuild on the primitives, or even for user defined types."),(0,o.kt)("p",null,"The simplest would be to look at some examples of ",(0,o.kt)("inlineCode",{parentName:"p"},"RVar")," usage. It is quite\ncommon to request several random numbers. ",(0,o.kt)("inlineCode",{parentName:"p"},"RVar")," provides several functions,\nwith ",(0,o.kt)("inlineCode",{parentName:"p"},"ints")," and ",(0,o.kt)("inlineCode",{parentName:"p"},"doubles")," being the most common for random variable\ncreation:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-scala"},"\nmdoc\nval ints = RVar.ints(5)\nval doubles = RVar.doubles(5)\n")),(0,o.kt)("p",null,"Both functions result in a ",(0,o.kt)("inlineCode",{parentName:"p"},"RVar")," that, when provided with a pseudo-random\nnumber generator (PRNG), will result in a list of values."),(0,o.kt)("p",null,"The user if free to define a PRNG for themselves, but CIlib provides a default\nPRNG that is suitable for scientific work. The CMWC generator may be\ninitialized by either providing a seed value for the pseudo-random number\nstream, or it may be taken from the current time of the computer. It is always\nrecommended to record the seed value, so that others may reproduce results,\nespecially if the results are to be published."),(0,o.kt)("p",null,"Let's create a ",(0,o.kt)("inlineCode",{parentName:"p"},"RNG")," instance using both methods:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-scala"},"\nmdoc\nval rng = RNG.init(1234L)\nval fromTimeYOLO = RNG.fromTime\n")),(0,o.kt)("p",null,"Now, let's run both ",(0,o.kt)("inlineCode",{parentName:"p"},"doubles")," and ",(0,o.kt)("inlineCode",{parentName:"p"},"ints")," with the generator:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-scala"},"\nmdoc\nval r1 = ints.run(rng)\ndoubles.run(rng)\nval r2 = ints.run(rng)\n\nr1._2 == r2._2\n")),(0,o.kt)("p",null,"The result is a tuple of the state of the PRNG after being used in the\ncomputation, together with the result of the computation itself. The\nimportant point to note is that running the computation again, with the\nsame PRNG, that is the original state of the PRNG ",(0,o.kt)("em",{parentName:"p"},"will")," result in the same\nobtained results. Unlike the normal PRNG within the JVM platform, obtaining\nsome random value from the source does not implicitly mutate the PRNG. In\norder to keep selecting from the PRNG stream, the next state of the PRNG\nshould be passed into subsequent computations, when needed:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-scala"},"\nmdoc\nval (rng2, x) = ints.run(rng)\n\nval (rng3, y) = ints.run(rng2)\n\nx != y\n")),(0,o.kt)("p",null,"This manual state passing for the PRNG is cumbersome and as a result,\nthe monad instance of ",(0,o.kt)("inlineCode",{parentName:"p"},"RVar")," provides this exact functionality to the user,\nthereby preventing accidental errors due to incorrect usage of PRNG state.\nFurthermore, the monad instance for ",(0,o.kt)("inlineCode",{parentName:"p"},"RVar")," allows for cleaner syntax through\nthe use of a ",(0,o.kt)("inlineCode",{parentName:"p"},"for"),"-comprehension as provided by Scala:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-scala"},"\nmdoc\nval composition = for {\n  a <- RVar.next[Int] // Get a single Int\n  b <- RVar.next[Double] // Get a single Double, using the next state of the PRNG\n  c <- RVar.next[Boolean] // Get a Boolean, again passing the PRNG state\n} yield if (c) a*b else b\n\ncomposition.run(rng)\n")),(0,o.kt)("p",null,"From this definition of how randomness is managed, we can derive several\nuseful algorithms which operate within the ",(0,o.kt)("inlineCode",{parentName:"p"},"RVar")," computation. Please refer\nto the ",(0,o.kt)("a",{parentName:"p",href:"http://ciren.github.io/cilib/api/cilib/RVar$.html"},"scaladoc")," for\nmore combinators, but some of the more commonly used are illustrated below:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-scala"},"\nmdoc\nval sampleList = NonEmptyList(6,4,5,2,1,3)\n\nRVar.shuffle(sampleList).runAll(rng)\nRVar.sample(3, sampleList).runAll(rng)\n")),(0,o.kt)("p",null,"Building on ",(0,o.kt)("inlineCode",{parentName:"p"},"RVar"),", we can easily define probability distributions from\nwhich, randomness may be sampled. The provided distributions, where\nstandard distributions are also defined, include:"),(0,o.kt)("ul",null,(0,o.kt)("li",{parentName:"ul"},"Uniform"),(0,o.kt)("li",{parentName:"ul"},"Gaussian / Normal"),(0,o.kt)("li",{parentName:"ul"},"Cauchy"),(0,o.kt)("li",{parentName:"ul"},"Gamma"),(0,o.kt)("li",{parentName:"ul"},"Exponential"),(0,o.kt)("li",{parentName:"ul"},"etc")),(0,o.kt)("p",null,"The interface for the distributions is simply a resulting ",(0,o.kt)("inlineCode",{parentName:"p"},"RVar")),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-scala"},"\nmdoc\n// Use a derived function from monad to repeat an action 'n' times\nDist.stdNormal.replicateM(5).run(rng)\n")))}d.isMDXComponent=!0}}]);