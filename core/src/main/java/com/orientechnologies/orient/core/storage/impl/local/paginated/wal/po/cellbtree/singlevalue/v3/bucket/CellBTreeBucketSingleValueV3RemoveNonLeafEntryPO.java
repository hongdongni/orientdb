package com.orientechnologies.orient.core.storage.impl.local.paginated.wal.po.cellbtree.singlevalue.v3.bucket;

import com.orientechnologies.common.serialization.types.OIntegerSerializer;
import com.orientechnologies.orient.core.storage.cache.OCacheEntry;
import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.WALRecordTypes;
import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.po.PageOperationRecord;
import com.orientechnologies.orient.core.storage.index.sbtree.singlevalue.v3.CellBTreeSingleValueBucketV3;
import java.nio.ByteBuffer;

public final class CellBTreeBucketSingleValueV3RemoveNonLeafEntryPO extends PageOperationRecord {
  private int index;

  private byte[] key;
  private int leftChild;
  private int rightChild;

  public CellBTreeBucketSingleValueV3RemoveNonLeafEntryPO() {}

  public CellBTreeBucketSingleValueV3RemoveNonLeafEntryPO(
      int index, byte[] key, int leftChild, int rightChild) {
    this.index = index;
    this.key = key;
    this.leftChild = leftChild;
    this.rightChild = rightChild;
  }

  public int getIndex() {
    return index;
  }

  public byte[] getKey() {
    return key;
  }

  public int getLeftChild() {
    return leftChild;
  }

  public int getRightChild() {
    return rightChild;
  }

  @Override
  public void redo(OCacheEntry cacheEntry) {
    final CellBTreeSingleValueBucketV3<?> bucket = new CellBTreeSingleValueBucketV3<>(cacheEntry);
    bucket.removeNonLeafEntry(index, key, false);
  }

  @Override
  public void undo(OCacheEntry cacheEntry) {
    final CellBTreeSingleValueBucketV3<?> bucket = new CellBTreeSingleValueBucketV3<>(cacheEntry);
    bucket.addNonLeafEntry(index, leftChild, rightChild, key);
  }

  @Override
  public int getId() {
    return WALRecordTypes.CELL_BTREE_BUCKET_SINGLE_VALUE_V3_REMOVE_NON_LEAF_ENTRY_PO;
  }

  @Override
  public int serializedSize() {
    return super.serializedSize() + 4 * OIntegerSerializer.INT_SIZE + key.length;
  }

  @Override
  protected void serializeToByteBuffer(ByteBuffer buffer) {
    super.serializeToByteBuffer(buffer);

    buffer.putInt(index);

    buffer.putInt(key.length);
    buffer.put(key);

    buffer.putInt(leftChild);
    buffer.putInt(rightChild);
  }

  @Override
  protected void deserializeFromByteBuffer(ByteBuffer buffer) {
    super.deserializeFromByteBuffer(buffer);

    index = buffer.getInt();

    final int keyLen = buffer.getInt();
    key = new byte[keyLen];
    buffer.get(key);

    leftChild = buffer.getInt();
    rightChild = buffer.getInt();
  }
}
