/**
 * Gulp tasks
 */
var gulp = require('gulp');
var gulpReplace = require('gulp-replace');

var bases = {
  src: 'src/',
  dist: 'dist/',
};

gulp.task('replace', function() {
  return gulp
    .src(['./dist/**/*.css'])
    .pipe(gulpReplace('/static/assets/', './../assets/'))
    .pipe(gulp.dest('./dist'));
});

// Generated files movement
gulp.task('copy', ['replace'], function() {
  gulp
    .src('./dist/**/*')
    .pipe(
      gulp.dest(
        '../gpcommercestorefront/web/webroot/_ui/responsive/common/dist/',
      ),
    )
    .pipe(
      gulp.dest('../gpb2bstorefront/web/webroot/_ui/responsive/common/dist/'),
    );
});

gulp.task('default', ['copy']);
