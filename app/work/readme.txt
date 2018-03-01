+ 범위 선택.
var selection = window.getSelection ();
if (selection.focusNode) {
    var rangeToSelect = document.createRange();
    rangeToSelect.selectNode (selection.focusNode);

    selection.removeAllRanges ();
    selection.addRange (rangeToSelect);

    var tr = selection.getRangeAt(0);
    var span = document.createElement("span");
    span.style.cssText = "color:#ff0000";
    tr.surroundContents(span);

    selection.removeAllRanges ();
}

+ 단어 선택
var selection = window.getSelection ();
if (selection.focusNode) {
    var fullStr = selection.focusNode.nodeValue;
    var s = 0;
    var e = 0;
    for ( var i = selection.focusOffset - 1; i >= 0; i-- ) {
        if ( fullStr.substring(i, i+1) == " " ) {
            s = i;
            break;
        }
    }
    for ( var i = selection.focusOffset; i < fullStr.length; i++ ) {
        if ( fullStr.substring(i, i+1) == " " ) {
            e = i;
            break;
        }
    }
    if ( s == 0 && e == 0 ) {
        e = selection.focusNode.length;
    }
console.log(s);
console.log(e);
console.log(fullStr.substring(s+1,e));
    var rangeToSelect = document.createRange();
    rangeToSelect.setStart(selection.focusNode, s);
    rangeToSelect.setEnd(selection.focusNode, e);
    selection.removeAllRanges ();
    selection.addRange (rangeToSelect);

    var tr = selection.getRangeAt(0);
    var span = document.createElement("span");
    span.style.cssText = "color:#ff0000";
    tr.surroundContents(span);

    selection.removeAllRanges ();
}