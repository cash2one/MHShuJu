(function(b){b.Sector=b.extend(b.Component,{configure:function(){b.Sector.superclass.configure.apply(this,arguments);this.type="sector";this.set({value:"",name:"",ignored:!1,counterclockwise:!1,startAngle:0,middleAngle:0,endAngle:0,totalAngle:0,bound_event:"click",expand:!1,donutwidth:0,mutex:!1,increment:void 0,label_length:void 0,gradient_mode:"RadialGradientOutIn",mini_label_threshold_angle:15,mini_label:!1,label:{},rounded:!1});this.atomic=!0;this.registerEvent("changed","parseText");this.tip=this.label=null},bound:function(){this.expanded||this.toggle()},rebound:function(){this.expanded&&this.toggle()},toggle:function(){this.fireEvent(this,this.get("bound_event"),[this])},getDimension:function(){var a=this._();return{x:a.x,x:a.y,startAngle:a.get("startAngle"),middleAngle:a.get("middleAngle"),endAngle:a.get("endAngle")}},doDraw:function(a){a.get("ignored")||(a.label&&!a.get("mini_label")&&a.label.draw(),a.drawSector(),a.label&&a.get("mini_label")&&a.label.draw())},doText:function(a,c,d){a.push("label.originx",c);a.push("label.originy",d);a.push("label.textBaseline","middle");a.label=new b.Text(a.get("label"),a)},doLabel:function(a,c,d,e,g,f,h,i){a.push("label.originx",c);a.push("label.originy",d);a.push("label.quadrantd",e);a.push("label.line_points",g);a.push("label.labelx",f);a.push("label.labely",h);a.push("label.smooth",i);a.push("label.angle",a.get("middleAngle")%(2*Math.PI));a.label=new b.Label(a.get("label"),a)},isLabel:function(){return this.get("label")&&!this.get("mini_label")},doConfig:function(){b.Sector.superclass.doConfig.call(this);var a=this._(),c=a.variable.event,d=a.get("label"),e=a.get("bound_event"),g;a.get("rounded")?(a.push("startAngle",0),a.push("endAngle",2*Math.PI)):(b.taylor.light(a,c),a.push("totalAngle",a.get("endAngle")-a.get("startAngle")),d&&(a.get("mini_label")&&(a.get("mini_label_threshold_angle")*Math.PI/180>a.get("totalAngle")?a.push("mini_label",!1):b.apply(a.get("label"),a.get("mini_label"))),a.push("label.text",a.fireString(a,"parseText",[a,a.get("label.text")],a.get("label.text"))),a.pushIf("label.border.color",a.get("border.color")),a.push("label.scolor",a.get("background_color"))),a.variable.event.status=a.expanded=a.get("expand"),a.get("tip.enable")&&("follow"!=a.get("tip.showType")&&a.push("tip.invokeOffsetDynamic",!1),a.tip=new b.Tip(a.get("tip"),a)),c.poped=!1,a.on(e,function(){c.poped=true;a.expanded=!a.expanded;a.redraw(e);c.poped=false}),a.on("beforedraw",function(b,h){if(h==e){g=false;a.x=a.get(a.X);a.y=a.get(a.Y);if(a.expanded)if(a.get("mutex")&&!c.poped){a.expanded=false;g=true}else{a.x=a.x+a.get("inc_x");a.y=a.y-a.get("inc_y")}if(c.status!=a.expanded){a.fireEvent(a,"changed",[a,a.expanded]);g=true;c.status=a.expanded}d&&g&&a.label.doLayout(a.get("inc_x")*(a.expanded?1:-1),-a.get("inc_y")*(a.expanded?1:-1),2,a.label)}return true}))}});b.Sector2D=b.extend(b.Sector,{configure:function(){b.Sector2D.superclass.configure.apply(this,arguments);this.type="sector2d";this.set({radius:0})},drawSector:function(){this.T.sector(this.x,this.y,this.r,this.get("donutwidth"),this.get("startAngle"),this.get("endAngle"),this.get("f_color"),this.get("border.enable"),this.get("border.width"),this.get("border.color"),this.get("shadow"),this.get("counterclockwise"))},isEventValid:function(a,c){if(!c.get("ignored")){if(c.isLabel()&&c.label.isEventValid(a,c.label).valid)return{valid:!0};var d=b.distanceP2P(c.x,c.y,a.x,a.y),e=c.get("donutwidth");if(c.r<d||e&&c.r-e>d)return{valid:!1};if(b.angleInRange(c.get("startAngle"),c.get("endAngle"),b.atan2Radian(c.x,c.y,a.x,a.y)))return{valid:!0}}return{valid:!1}},tipInvoke:function(){var a=this,c=a.get("middleAngle"),d=b.quadrantd(c);return function(e,g){var f=b.p2Point(a.x,a.y,c,0.8*a.r);return{left:1<=d&&2>=d?f.x-e:f.x,top:2<=d?f.y-g:f.y}}},doConfig:function(){b.Sector2D.superclass.doConfig.call(this);var a=this._();a.r=a.get("radius");a.get("donutwidth")>a.r&&a.push("donutwidth",0);a.applyGradient(a.x-a.r,a.y-a.r,1.8*a.r,1.8*a.r);var c=a.get("middleAngle"),d=a.pushIf("increment",b.lowTo(5,a.r/10));a.push("inc_x",d*Math.cos(2*Math.PI-c));a.push("inc_y",d*Math.sin(2*Math.PI-c));d*=2;if(a.get("label"))if(a.get("mini_label"))P2=b.p2Point(a.x,a.y,c,a.get("donutwidth")?a.r-a.get("donutwidth")/2:5*a.r/8),a.doText(a,P2.x,P2.y);else{var e=b.quadrantd(c),g=b.p2Point(a.x,a.y,c,a.r+d),f=b.p2Point(a.x,a.y,c,a.r+0.6*d);P2=b.p2Point(a.x,a.y,c,a.r);a.doLabel(a,P2.x,P2.y,e,[{x:P2.x,y:P2.y},{x:f.x,y:f.y},{x:g.x,y:g.y}],g.x,g.y,0.4*d)}}});b.Sector3D=b.extend(b.Sector,{configure:function(){b.Sector3D.superclass.configure.apply(this,arguments);this.type="sector3d";this.dimension=b._3D;this.set({semi_major_axis:0,semi_minor_axis:0,cylinder_height:0});this.proxy=!0},isEventValid:function(a,c){if(!c.get("ignored")){if(c.isLabel()&&c.label.isEventValid(a,c.label).valid)return{valid:!0};if(!b.inEllipse(a.x-c.x,a.y-c.y,c.a,c.b))return{valid:!1};if(b.angleZInRange(c.sA,c.eA,b.atan2Radian(c.x,c.y,a.x,a.y)))return{valid:!0}}return{valid:!1}},p2p:function(a,c,b,e){return{x:a+this.a*Math.cos(b)*e,y:c+this.b*Math.sin(b)*e}},tipInvoke:function(){var a=this,c=a.get("middleAngle"),d=b.quadrantd(c);return function(b,g){var f=a.p2p(a.x,a.y,c,0.6);return{left:2<=d&&3>=d?f.x-b:f.x,top:3<=d?f.y-g:f.y}}},doConfig:function(){b.Sector3D.superclass.doConfig.call(this);var a=this._(),c=a.get("counterclockwise"),d=a.get("middleAngle");a.a=a.get("semi_major_axis");a.b=a.get("semi_minor_axis");a.h=a.get("cylinder_height");b.Assert.isTrue(0<=a.a*a.b,"major&minor");var e=2*Math.PI,g=function(i){for(;0>i;)i+=e;return Math.abs(b.atan2Radian(0,0,a.a*Math.cos(i),c?-a.b*Math.sin(i):a.b*Math.sin(i)))},f=a.pushIf("increment",b.lowTo(5,a.a/10));a.sA=g.call(a,a.get("startAngle"));a.eA=g.call(a,a.get("endAngle"));a.mA=g.call(a,d);a.push("inc_x",f*Math.cos(e-a.mA));a.push("inc_y",f*Math.sin(e-a.mA));f*=2;if(a.get("label"))if(a.get("mini_label"))f=a.p2p(a.x,a.y,d,0.5),a.doText(a,f.x,f.y);else{var g=b.quadrantd(d),h=a.p2p(a.x,a.y,d,f/a.a+1),i=a.p2p(a.x,a.y,d,0.6*f/a.a+1),d=a.p2p(a.x,a.y,d,1);a.doLabel(a,d.x,d.y,g,[{x:d.x,y:d.y},{x:i.x,y:i.y},{x:h.x,y:h.y}],h.x,h.y,0.4*f)}}});b.Label=b.extend(b.Component,{configure:function(){b.Label.superclass.configure.apply(this,arguments);this.type="label";this.set({text:"",line_height:12,line_thickness:1,sign:"square",sign_size:12,padding:"2 5",offsety:2,sign_space:5,background_color:"#efefef",text_with_sign_color:!1});this.atomic=!0;this.registerEvent()},isEventValid:function(a,c){return{valid:b.inRange(c.labelx,c.labelx+c.get(c.W),a.x)&&b.inRange(c.labely,c.labely+c.get(c.H),a.y)}},text:function(a){a&&this.push("text",a);this.push(this.W,this.T.measureText(this.get("text"))+this.get("hpadding")+this.get("sign_size")+this.get("sign_space"))},localizer:function(a){var c=a.get("quadrantd"),b=a.get("line_points"),e=a.get("smooth"),c=1<=c&&2>=c,g=a.get("labelx"),f=a.get("labely");a.labelx=g+(c?-a.get(a.W)-e:e);a.labely=f-a.get(a.H)/2;b[2]={x:g,y:f};b[3]={x:b[2].x+(c?-e:e),y:b[2].y}},doLayout:function(a,c,b,e){e.push("labelx",e.get("labelx")+a/b);e.push("labely",e.get("labely")+c/b);e.get("line_points").each(function(b,d){b.x+=a;b.y+=c;return 1==d},e);e.localizer(e)},doDraw:function(a){var c=a.get("line_points"),b=a.get("sign_size"),e=a.labelx+a.get("padding_left"),g=a.labely+a.get("padding_top");a.T.label(c,a.get("line_thickness"),a.get("border.color"));a.T.box(a.labelx,a.labely,a.get(a.W),a.get(a.H),a.get("border"),a.get("f_color"),!1,a.get("shadow"));a.T.textStyle(a.L,a.O,a.get("fontStyle"));c=a.get("color");a.get("text_with_sign_color")&&(c=a.get("scolor"));"square"==a.get("sign")?a.T.box(e,g,b,b,0,a.get("scolor")):a.get("sign")&&a.T.round(e+b/2,g+b/2,b/2,a.get("scolor"));a.T.fillText(a.get("text"),e+b+a.get("sign_space"),g,a.get("textwidth"),c)},doConfig:function(){b.Label.superclass.doConfig.call(this);var a=this._();a.T.textFont(a.get("fontStyle"));a.get("fontsize")>a.get("line_height")&&a.push("line_height",a.get("fontsize"));a.get("sign")||(a.push("sign_size",0),a.push("sign_space",0));a.push(a.H,a.get("line_height")+a.get("vpadding"));a.text();a.localizer(a)}});b.Pie=b.extend(b.Chart,{configure:function(){b.Pie.superclass.configure.call(this);this.type="pie";this.set({radius:"100%",offset_angle:0,separate_angle:0,bound_event:"click",counterclockwise:!1,intellectLayout:!0,layout_distance:4,pop_animate:!1,mutex:!1,increment:void 0,sub_option:{label:{}}});this.registerEvent("bound","rebound");this.sectors=[];this.components.push(this.sectors);this.ILLUSIVE_COO=!0},toggle:function(a){this.sectors[a||0].toggle()},bound:function(a){this.sectors[a||0].bound()},rebound:function(a){this.sectors[a||0].rebound()},getSectors:function(){return this.sectors},doAnimation:function(a,b,d){var e=0,g=d.oA;d.sectors.each(function(f){e=d.animationArithmetic(a,0,f.get("totalAngle"),b);f.push("startAngle",g);f.push("endAngle",g+=e);d.is3D()||f.drawSector()});d.is3D()&&d.proxy.drawSector()},parse:function(a){a.data.each(function(b,d){a.doParse(a,b,d)},a);a.localizer(a)},doParse:function(a,b,d){var e=b.name+" "+a.getPercent(b.value);a.doActing(a,b,null,d,e);a.push("sub_option.id",d);a.get("sub_option.label")&&a.push("sub_option.label.text",e);a.push("sub_option.listeners.changed",function(b,c){a.fireEvent(a,c?"bound":"rebound",[a,b.get("name")])});a.sectors.push(a.doSector(a,b))},doSector:function(a){return new b[a.sub](a.get("sub_option"),a)},dolayout:function(a,c,d,e,g,f){if(a.is3D()?b.inEllipse(a.get(a.X)-c,a.topY-d,a.a,a.b):b.distanceP2P(a.get(a.X),a.topY,c,d)<a.r)d=a.topY-d,e.push("labelx",a.get(a.X)+(2*Math.sqrt(a.r*a.r-d*d)+g)*(0==f||3==f?1:-1)),e.localizer(e)},localizer:function(a){if(a.get("intellectLayout")){var b=[],d=[],e=a.get("layout_distance"),g,f,h;a.sectors.each(function(a){a.isLabel()&&b.push(a.label)});b.sor(function(a,b){return 0<Math.abs(Math.sin(a.get("angle")))-Math.abs(Math.sin(b.get("angle")))});b.each(function(b){d.each(function(c){f=c.labelx;h=c.labely;if(b.labely<=h&&h-b.labely-1<b.get(a.H)||b.labely>h&&b.labely-h-1<c.get(a.H))if(b.labelx<=f&&f-b.labelx<b.get(a.W)||b.labelx>f&&b.labelx-f<c.get(a.W))g=b.get("quadrantd"),b.push("labely",b.get("labely")+h-b.labely+(b.get(a.H)+e)*(1<g?-1:1)),b.localizer(b),a.dolayout(a,b.get("labelx"),b.get("labely")+b.get(a.H)/2*(2>g?-1:1),b,e,g)},a);d.push(b)})}},doConfig:function(){b.Pie.superclass.doConfig.call(this);var a=this._(),c,d=a.get("radius"),e=a.get("sub_option.label")?0.35:0.44,g=2*Math.PI;a.sub=a.is3D()?"Sector3D":"Sector2D";a.sectors.zIndex=a.get("z_index");a.sectors.length=0;a.oA=b.angle2Radian(a.get("offset_angle"))%g;a.is3D()&&(e+=0.06);var f=a.data.length,h=b.angle2Radian(b.between(0,90,a.get("separate_angle"))),i=g-h,h=h/f,j=a.oA+h,k=j;0==a.total&&(c=1/f);a.data.each(function(b,d){j+=(c||b.value/a.total)*i;d==f-1&&(j=g+a.oA);b.startAngle=k;b.endAngle=j;b.totalAngle=j-k;b.middleAngle=(k+j)/2;k=j+h},a);a.r=d=b.parsePercent(d,Math.floor(a.get("minDistance")*e));a.topY=a.originXY(a,[d+a.get("l_originx"),a.get("r_originx")-d,a.get("centerx")],[a.get("centery")]).y;b.apply(a.get("sub_option"),b.clone([a.X,a.Y,"bound_event","mutex","increment"],a.options))}});b.Pie2D=b.extend(b.Pie,{configure:function(){b.Pie2D.superclass.configure.call(this);this.type="pie2d"},doConfig:function(){b.Pie2D.superclass.doConfig.call(this);var a=this._();a.push("sub_option.radius",a.r);a.parse(a)}});b.register("Pie2D");b.Donut2D=b.extend(b.Pie,{configure:function(){b.Donut2D.superclass.configure.call(this);this.type="donut2d";this.set({donutwidth:0.3,center:{text:"",line_height:24,fontweight:"bold",fontsize:24}})},doConfig:function(){b.Donut2D.superclass.doConfig.call(this);var a=this._(),c=a.r;a.push("sub_option.radius",c);0<a.get("donutwidth")&&(1>a.get("donutwidth")?a.push("donutwidth",Math.floor(c*a.get("donutwidth"))):a.get("donutwidth")>=c&&a.push("donutwidth",0),a.push("sub_option.donutwidth",a.get("donutwidth")));b.isString(a.get("center"))&&a.push("center",b.applyIf({text:a.get("center")},a.default_.center));""!=a.get("center.text")&&(a.push("center.originx",a.get(a.X)),a.push("center.originy",a.get(a.Y)),a.push("center.textBaseline","middle"),a.center=new b.Text(a.get("center"),a),a.components.push(a.center));a.parse(a)}});b.register("Donut2D");b.Pie3D=b.extend(b.Pie,{configure:function(){b.Pie3D.superclass.configure.apply(this,arguments);this.type="pie3d";this.dimension=b._3D;this.set({zRotate:45,yHeight:30});this.positive=!0},doSector:function(a,c){a.push("sub_option.cylinder_height",c.cylinder_height?c.cylinder_height*a.get("zRotate_"):a.get("cylinder_height"));return new b[a.sub](a.get("sub_option"),a)},one:function(a){var c,d,e=[],g=a.get("counterclockwise"),f=function(a,b){return 1+Math.sin(b?a+Math.PI:a)},h;lay=function(a,d,e,f){h=b.quadrantd(d);(a&&(0==h||3==h)||!a&&(2==h||1==h))&&c.push({g:d,z:d==e,x:f.x,y:f.y,a:f.a,b:f.b,color:b.dark(f.get("background_color")),h:f.h,F:f})};a.proxy=new b.Custom({z_index:a.get("z_index")+1,drawFn:function(){this.drawSector();e=[];a.sectors.each(function(a){a.get("label")&&(a.expanded?e.push(a.label):a.label.draw())});e.each(function(a){a.draw()})}});a.proxy.drawSector=function(){a.sectors.each(function(b){a.T.ellipse(b.x,b.y+b.h,b.a,b.b,b.get("startAngle"),b.get("endAngle"),0,b.get("border.enable"),b.get("border.width"),b.get("border.color"),b.get("shadow"),g,!0)},a);c=[];d=[];a.sectors.each(function(a){lay(g,a.get("startAngle"),a.get("endAngle"),a);lay(!g,a.get("endAngle"),a.get("startAngle"),a);d=d.concat(b.visible(a.get("startAngle"),a.get("endAngle"),a))},a);c.sor(function(a,b){var c=f(a.g)-f(b.g);return 0==c?a.z:0<c});c.each(function(b){a.T.sector3D.layerDraw.call(a.T,b.x,b.y,b.a+0.5,b.b+0.5,g,b.h,b.g,b.color)},a);a.processAnimation||d.sor(function(a,b){return 0>f((a.s+a.e)/2,1)-f((b.s+b.e)/2,1)});d.each(function(b){a.T.sector3D.sPaint.call(a.T,b.f.x,b.f.y,b.f.a,b.f.b,b.s,b.e,g,b.f.h,b.f.get("f_color"))},a);a.sectors.each(function(b){a.T.ellipse(b.x,b.y,b.a,b.b,b.get("startAngle"),b.get("endAngle"),b.get("f_color"),b.get("border.enable"),b.get("border.width"),b.get("border.color"),!1,!1,!0)},a)};a.one=b.emptyFn},doConfig:function(){b.Pie3D.superclass.doConfig.call(this);var a=this._(),c=b.angle2Radian(a.get("zRotate"));a.push("cylinder_height",a.get("yHeight")*a.push("zRotate_",Math.abs(Math.cos(c))));a.a=a.push("sub_option.semi_major_axis",a.r);a.b=a.push("sub_option.semi_minor_axis",a.r*Math.abs(Math.sin(c)));a.topY=a.push("sub_option.originy",a.get(a.Y)-a.get("yHeight")/2);a.parse(a);a.one(a);a.components.push(a.proxy)}});b.register("Pie3D")})(iChart);