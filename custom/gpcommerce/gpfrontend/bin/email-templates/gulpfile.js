const gulp = require('gulp');
const handlebars = require('gulp-compile-handlebars');
const rename = require('gulp-rename');

function defaultTask() {
  const options = {
    ignorePartials: true,
    batch: ['./partials'],
  };

  return gulp
    .src('index.hbs')
    .pipe(handlebars({}, options))
    .pipe(rename('main.html'))
    .pipe(gulp.dest('main'));
}

gulp.task('default', defaultTask);
