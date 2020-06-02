package org.tron.core.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tron.common.utils.ByteUtil;
import org.tron.core.capsule.PbftSignCapsule;
import org.tron.protos.Protocol.PBFTMessage.DataType;

@Slf4j
@Component
public class PbftSignDataStore extends TronDatabase<PbftSignCapsule> {

  public PbftSignDataStore() {
    super("pbft-sign-data");
  }

  @Override
  public void put(byte[] key, PbftSignCapsule item) {
    dbSource.putData(key, item.getData());
  }

  @Override
  public PbftSignCapsule get(byte[] key) {
    byte[] data = dbSource.getData(key);
    if (ByteUtil.isNullOrZeroArray(data)) {
      return null;
    }
    return new PbftSignCapsule(data);
  }

  @Override
  public void delete(byte[] key) {
    dbSource.deleteData(key);
  }

  @Override
  public boolean has(byte[] key) {
    return dbSource.getData(key) != null;
  }

  public void putSrSignData(long epoch, PbftSignCapsule item) {
    put(buildSrSignKey(epoch), item);
  }

  public PbftSignCapsule getSrSignData(long epoch) {
    return get(buildSrSignKey(epoch));
  }

  public PbftSignCapsule getCrossSrSignData(String chainId, long epoch) {
    return get(buildCrossSrSignKey(chainId, epoch));
  }

  public void putCrossSrSignData(String chainId, long epoch, PbftSignCapsule item) {
    put(buildCrossSrSignKey(chainId, epoch), item);
  }

  public void putBlockSignData(long blockNum, PbftSignCapsule item) {
    put(buildBlockSignKey(blockNum), item);
  }

  public PbftSignCapsule getBlockSignData(long blockNum) {
    return get(buildBlockSignKey(blockNum));
  }

  public void putCrossBlockSignData(String chainId, long blockNum, PbftSignCapsule item) {
    put(buildCrossBlockSignKey(chainId, blockNum), item);
  }

  public PbftSignCapsule getCrossBlockSignData(String chainId, long blockNum) {
    return get(buildCrossBlockSignKey(chainId, blockNum));
  }


  //todo:use type
  private byte[] buildSrSignKey(long epoch) {
    return (DataType.SRL.toString() + epoch).getBytes();
  }

  private byte[] buildCrossSrSignKey(String chainId, long epoch) {
    return (DataType.SRL.toString() + chainId + epoch).getBytes();
  }

  private byte[] buildBlockSignKey(long blockNum) {
    return (DataType.BLOCK.toString() + blockNum).getBytes();
  }

  private byte[] buildCrossBlockSignKey(String chainId, long blockNum) {
    return (DataType.BLOCK.toString() + chainId + blockNum).getBytes();
  }
}
