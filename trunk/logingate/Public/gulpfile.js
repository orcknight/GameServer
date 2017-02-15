var gulp = require('gulp'),
    minifycss = require('gulp-minify-css'),
    concat = require('gulp-concat'),
    notify = require('gulp-notify'),
    uglify = require('gulp-uglify'),
    rename = require('gulp-rename'),
    del = require('del');


gulp.task('minifycss', function() {
    return gulp.src(['./source/debug/styles/sm.css', 
        './source/debug/styles/sm-extend.css', 
        './source/debug/styles/main.css', 
        './source/debug/styles/biz.css'])
        .pipe(concat('luobo.css'))   
        .pipe(gulp.dest('./source/dist'))
        .pipe(rename({ suffix: '.min' }))
        .pipe(minifycss())
        .pipe(gulp.dest('./source/dist'))
        .pipe(notify({ message: 'Styles task complete' }));
});


gulp.task('minifyjs', function() {
    return gulp.src(['./source/debug/scripts/zepto.js', 
        './source/debug/scripts/sm.js', 
        './source/debug/scripts/sm-extend.js', 
        './source/debug/scripts/template-debug.js', 
        './source/debug/scripts/swiper.js', 
        './source/debug/scripts/webuploader.custom.js'])
        .pipe(concat('luobo.js'))    //合并所有js到main.js
        .pipe(gulp.dest('./source/dist'))    //输出main.js到文件夹
        .pipe(rename({suffix: '.min'}))   //rename压缩后的文件名
        .pipe(uglify())    //压缩
        .pipe(gulp.dest('./source/dist'))
        .pipe(notify({ message: 'Scripts task complete' }));
});

gulp.task('clean', function() {
    del(['./source/dist/*.*']);
});

gulp.task('default', ['clean'], function() {
    console.log(">>>>>>start.");
    gulp.start('minifycss', 'minifyjs');
    console.log(">>>>>>end.");
});


