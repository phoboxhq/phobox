
export default class Swipe {

  constructor(element, onLeft, onRight, onUp, onDown) {
    element.addEventListener('touchstart', this.handleTouchStart, false);
    element.addEventListener('touchmove', this.handleTouchMove, false);

    this.xDown = null;
    this.yDown = null;

    this.onLeft = onLeft;
    this.onRight = onRight;
    this.onUp = onUp;
    this.onDown = onDown;
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
        if(this.onLeft !== null || this.onLeft !== null)
        this.onLeft();
      } else {
        if(this.onRight !== null || this.onRight !== null)
        this.onRight();
      }
    } else {
      if (yDiff > 0) {
        if(this.onUp !== null || this.onUp !== null)
          this.onUp();
      } else {
        if(this.onDown !== null || this.onDown !== null)
          this.onDown();
      }
    }
    /* reset values */
    this.xDown = null;
    this.yDown = null;
  }
}