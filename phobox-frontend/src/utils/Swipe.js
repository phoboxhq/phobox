
export default class Swipe {

  constructor(element, onLeft, onRight, onUp, onDown) {
    element.addEventListener('touchstart', handleTouchStart, false);
    element.addEventListener('touchmove', handleTouchMove, false);

    this.xDown = null;
    this.yDown = null;
  }

  handleTouchStart(evt) {
    this.xDown = evt.touches[0].clientX;
    this.yDown = evt.touches[0].clientY;
  }

  handleTouchMove(evt) {
    if (!this.xDown || !this.yDown) {
      return;
    }

    let xUp = evt.touches[0].clientX;
    let yUp = evt.touches[0].clientY;

    let xDiff = this.xDown - xUp;
    let yDiff = this.yDown - yUp;

    if (Math.abs(xDiff) > Math.abs(yDiff)) {/*most significant*/
      if (xDiff > 0) {
        onLeft();
      } else {
        onRight();
      }
    } else {
      if (yDiff > 0) {
        onUp();
      } else {
        onDown();
      }
    }
    /* reset values */
    this.xDown = null;
    this.yDown = null;
  }
}