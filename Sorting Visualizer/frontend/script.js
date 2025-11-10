const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));

function renderBars(array, highlight = []) {
  const container = document.getElementById("array-container");
  container.innerHTML = "";

  array.forEach((value, idx) => {
    const bar = document.createElement("div");
    bar.className = "bar";
    bar.style.height = `${value * 4}px`;
    bar.innerHTML = `<span>${value}</span>`;
    if (highlight.includes(idx)) bar.style.backgroundColor = "#e91e63";
    container.appendChild(bar);
  });
}

async function startSort() {
  const input = document.getElementById("inputArray").value.trim();
  const algo = document.getElementById("algo").value;
  const output = document.getElementById("output");
  output.innerHTML = "";

  if (!input) {
    alert("Please enter numbers");
    return;
  }

  const array = input.split(",").map(x => parseInt(x.trim())).filter(x => !isNaN(x));
  if (array.length < 2) return alert("Need at least 2 numbers");

  let comparisons = 0;
  renderBars(array);

  const swap = (arr, i, j) => {
    [arr[i], arr[j]] = [arr[j], arr[i]];
  };

  async function bubbleSort(arr) {
    for (let i = 0; i < arr.length; i++) {
      for (let j = 0; j < arr.length - i - 1; j++) {
        renderBars(arr, [j, j+1]);
        comparisons++;
        if (arr[j] > arr[j+1]) {
          swap(arr, j, j+1);
          await delay(300);
        }
      }
    }
  }

  async function selectionSort(arr) {
    for (let i = 0; i < arr.length; i++) {
      let minIdx = i;
      for (let j = i + 1; j < arr.length; j++) {
        comparisons++;
        renderBars(arr, [minIdx, j]);
        if (arr[j] < arr[minIdx]) minIdx = j;
        await delay(300);
      }
      swap(arr, i, minIdx);
    }
  }

  async function insertionSort(arr) {
    for (let i = 1; i < arr.length; i++) {
      let key = arr[i];
      let j = i - 1;
      while (j >= 0 && arr[j] > key) {
        comparisons++;
        arr[j + 1] = arr[j];
        j--;
        renderBars(arr, [j + 1, i]);
        await delay(300);
      }
      arr[j + 1] = key;
    }
  }

  async function mergeSort(arr, l = 0, r = arr.length - 1) {
    if (l >= r) return;

    const m = Math.floor((l + r) / 2);
    await mergeSort(arr, l, m);
    await mergeSort(arr, m + 1, r);
    await merge(arr, l, m, r);
  }

  async function merge(arr, l, m, r) {
    let left = arr.slice(l, m + 1);
    let right = arr.slice(m + 1, r + 1);
    let i = 0, j = 0, k = l;

    while (i < left.length && j < right.length) {
      comparisons++;
      if (left[i] <= right[j]) {
        arr[k++] = left[i++];
      } else {
        arr[k++] = right[j++];
      }
      renderBars(arr, [k - 1]);
      await delay(300);
    }

    while (i < left.length) arr[k++] = left[i++];
    while (j < right.length) arr[k++] = right[j++];
  }

  async function quickSort(arr, low = 0, high = arr.length - 1) {
    if (low < high) {
      let pi = await partition(arr, low, high);
      await quickSort(arr, low, pi - 1);
      await quickSort(arr, pi + 1, high);
    }
  }

  async function partition(arr, low, high) {
    let pivot = arr[high];
    let i = low - 1;
    for (let j = low; j < high; j++) {
      comparisons++;
      if (arr[j] < pivot) {
        i++;
        swap(arr, i, j);
        renderBars(arr, [i, j]);
        await delay(300);
      }
    }
    swap(arr, i + 1, high);
    return i + 1;
  }

  async function heapSort(arr) {
    const n = arr.length;

    async function heapify(n, i) {
      let largest = i;
      let l = 2 * i + 1;
      let r = 2 * i + 2;

      if (l < n && arr[l] > arr[largest]) largest = l;
      if (r < n && arr[r] > arr[largest]) largest = r;

      comparisons++;
      if (largest !== i) {
        swap(arr, i, largest);
        renderBars(arr, [i, largest]);
        await delay(300);
        await heapify(n, largest);
      }
    }

    for (let i = Math.floor(n / 2) - 1; i >= 0; i--) await heapify(n, i);
    for (let i = n - 1; i > 0; i--) {
      swap(arr, 0, i);
      renderBars(arr, [0, i]);
      await delay(300);
      await heapify(i, 0);
    }
  }

  async function countingSort(arr) {
    let max = Math.max(...arr);
    let count = new Array(max + 1).fill(0);
    let output = new Array(arr.length);

    for (let num of arr) count[num]++;
    for (let i = 1; i <= max; i++) count[i] += count[i - 1];

    for (let i = arr.length - 1; i >= 0; i--) {
      output[count[arr[i]] - 1] = arr[i];
      count[arr[i]]--;
      comparisons++;
    }

    for (let i = 0; i < arr.length; i++) {
      arr[i] = output[i];
      renderBars(arr, [i]);
      await delay(100);
    }
  }

  async function radixSort(arr) {
    const getMax = arr => Math.max(...arr);

    const countingSortByDigit = async (arr, exp) => {
      let output = new Array(arr.length).fill(0);
      let count = new Array(10).fill(0);

      for (let i = 0; i < arr.length; i++) count[Math.floor(arr[i] / exp) % 10]++;
      for (let i = 1; i < 10; i++) count[i] += count[i - 1];

      for (let i = arr.length - 1; i >= 0; i--) {
        let index = Math.floor(arr[i] / exp) % 10;
        output[count[index] - 1] = arr[i];
        count[index]--;
        comparisons++;
      }

      for (let i = 0; i < arr.length; i++) {
        arr[i] = output[i];
        renderBars(arr, [i]);
        await delay(100);
      }
    };

    for (let exp = 1; Math.floor(getMax(arr) / exp) > 0; exp *= 10) {
      await countingSortByDigit(arr, exp);
    }
  }

  async function bucketSort(arr) {
    let n = arr.length;
    let max = Math.max(...arr);
    let size = Math.floor(Math.sqrt(n)) + 1;
    let buckets = Array.from({ length: size }, () => []);

    for (let i = 0; i < n; i++) {
      buckets[Math.floor((arr[i] * size) / (max + 1))].push(arr[i]);
    }

    let idx = 0;
    for (let bucket of buckets) {
      bucket.sort((a, b) => a - b);
      for (let val of bucket) {
        arr[idx++] = val;
        renderBars(arr, [idx - 1]);
        await delay(100);
      }
    }
  }

  const algoMap = {
    bubble: bubbleSort,
    selection: selectionSort,
    insertion: insertionSort,
    merge: mergeSort,
    quick: quickSort,
    heap: heapSort,
    counting: countingSort,
    radix: radixSort,
    bucket: bucketSort
  };

  const complexityMap = {
    bubble: "O(n²)", selection: "O(n²)", insertion: "O(n²)",
    merge: "O(n log n)", quick: "O(n log n)", heap: "O(n log n)",
    counting: "O(n + k)", radix: "O(nk)", bucket: "O(n + k)"
  };

  await algoMap[algo](array);

  output.innerHTML = `
    <p><strong>Sorted Array:</strong> ${array.join(", ")}</p>
    <p><strong>Comparisons:</strong> ${comparisons}</p>
    <p><strong>Time Complexity:</strong> ${complexityMap[algo]}</p>
  `;
}
