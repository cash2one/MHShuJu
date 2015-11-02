Handlebars.registerHelper('divide100', function(value){
	return value/100;
});
Handlebars.registerHelper('add', function(value, addition){
	return value + addition;
});
Handlebars.registerHelper('subtract', function(value, substraction){
	return value - substraction;
});
Handlebars.registerHelper('divide', function(value, divisor){
	return value / divisor
});
Handlebars.registerHelper('multiply', function(value, multiplier){
	return value * multiplier
});
Handlebars.registerHelper('floor', function(value){
	return Math.floor(value);
});
Handlebars.registerHelper('ceil', function(value){
	return Math.ceil(value);
});
Handlebars.registerHelper('round', function(value){
	return Math.round(value);
});
/**
 * If Equals
 * if_eq this compare=that
 */
Handlebars.registerHelper('if_eq', function(context, options) {
  if (context == options.hash.compare)
    return options.fn(this);
  return options.inverse(this);
});

/**
 * Unless Equals
 * unless_eq this compare=that
 */
Handlebars.registerHelper('unless_eq', function(context, options) {
  if (context == options.hash.compare)
    return options.inverse(this);
  return options.fn(this);
});


/**
 * If Greater Than
 * if_gt this compare=that
 */
Handlebars.registerHelper('if_gt', function(context, options) {
  if (context > options.hash.compare)
    return options.fn(this);
  return options.inverse(this);
});

/**
 * Unless Greater Than
 * unless_gt this compare=that
 */
Handlebars.registerHelper('unless_gt', function(context, options) {
  if (context > options.hash.compare)
    return options.inverse(this);
  return options.fn(this);
});


/**
 * If Less Than
 * if_lt this compare=that
 */
Handlebars.registerHelper('if_lt', function(context, options) {
  if (context < options.hash.compare)
    return options.fn(this);
  return options.inverse(this);
});

/**
 * Unless Less Than
 * unless_lt this compare=that
 */
Handlebars.registerHelper('unless_lt', function(context, options) {
  if (context < options.hash.compare)
    return options.inverse(this);
  return options.fn(this);
});


/**
 * If Greater Than or Equal To
 * if_gteq this compare=that
 */
Handlebars.registerHelper('if_gteq', function(context, options) {
  if (context >= options.hash.compare)
    return options.fn(this);
  return options.inverse(this);
});

/**
 * Unless Greater Than or Equal To
 * unless_gteq this compare=that
 */
Handlebars.registerHelper('unless_gteq', function(context, options) {
  if (context >= options.hash.compare)
    return options.inverse(this);
  return options.fn(this);
});


/**
 * If Less Than or Equal To
 * if_lteq this compare=that
 */
Handlebars.registerHelper('if_lteq', function(context, options) {
  if (context <= options.hash.compare)
    return options.fn(this);
  return options.inverse(this);
});

/**
 * Unless Less Than or Equal To
 * unless_lteq this compare=that
 */
Handlebars.registerHelper('unless_lteq', function(context, options) {
  if (context <= options.hash.compare)
    return options.inverse(this);
  return options.fn(this);
});

/**
 *  裁剪内容,对于过长的string,使用...来替代
 *  参数：内容,保留长度
 */
Handlebars.registerHelper("shrink", function(content,length) {
	content = content ? content : '';
	if(content.length > length){
		content = content.slice(0,length);
		content += '..';
	}
	return new Handlebars.SafeString(content);
});


Handlebars.registerHelper("subStr", function(content,start,length) {
    content = content ? content : '';
    if(content.length > length && content.length>start){
        content = content.slice(start,length);
    }
    return new Handlebars.SafeString(content);
});

Handlebars.registerHelper("ternary", function(content,value) {
	content = content ? content : value;
	return new Handlebars.SafeString(content);
});

/**
 * SafeString
 */
Handlebars.registerHelper('safeString', function(text,options) {
	text = text ? text : '';
	return new Handlebars.SafeString(text);
});

/**
 * 定义表格隔行变色
 */
Handlebars.registerHelper("splitClass", function(txt,fn) {
	var buffer = "split";
	if( txt % 2 == 0  ){
		buffer = "";
	}
	return buffer;
});

/**
 * 判断数组中是否有多个数组项目
 * 
 */
Handlebars.registerHelper("if_array_multi", function(array, options) {
	array = array?array:[''];
	if(array.length && array.length>1){
		return options.fn(this);
	} else {
		return options.inverse(this);
	}
});