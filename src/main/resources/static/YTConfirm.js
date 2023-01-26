function validVideoId(id) {
  var img = new Image();
  img.src = "http://img.youtube.com/vi/" + id + "/mqdefault.jpg";
  img.onload = function () {
    checkThumbnail(this.width);
  };
}

function checkThumbnail(width) {
  //HACK a mq thumbnail has width of 320.
  //if the video does not exist(therefore thumbnail don't exist), a default thumbnail of 120 width is returned.
  width === 120 ? true : false;
}
