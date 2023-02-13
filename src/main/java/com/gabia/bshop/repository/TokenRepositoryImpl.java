// package com.gabia.bshop.repository;
//
// import java.util.Map;
// import java.util.Optional;
//
// import org.springframework.data.redis.core.HashOperations;
// import org.springframework.data.redis.hash.HashMapper;
// import org.springframework.stereotype.Repository;
// import org.springframework.transaction.annotation.Transactional;
//
// import com.gabia.bshop.security.redis.RefreshToken;
//
// import lombok.RequiredArgsConstructor;
//
// @RequiredArgsConstructor
// @Transactional(readOnly = true)
// @Repository
// public class TokenRepositoryImpl implements TokenRepository {
//
// 	private final HashOperations<String, byte[], byte[]> hashOperations;
// 	private final HashMapper<Object, byte[], byte[]> hashMapper;
//
// 	@Override
// 	@Transactional
// 	public RefreshToken save(final RefreshToken refreshToken) {
// 		final String key = refreshToken.refreshToken();
// 		final Map<byte[], byte[]> mappedHash = hashMapper.toHash(refreshToken);
// 		hashOperations.putAll(key, mappedHash);
// 		return refreshToken;
// 	}
//
// 	@Override
// 	public Optional<RefreshToken> findToken(final String savedTokenValue) {
// 		final Map<byte[], byte[]> loadedHash = hashOperations.entries(savedTokenValue);
// 		if (loadedHash == null) {
// 			System.out.println("null");
// 		}
// 		return Optional.of((RefreshToken)hashMapper.fromHash(loadedHash));
// 	}
//
// 	@Override
// 	public void delete(final String savedTokenValue) {
// 		final Map<byte[], byte[]> loadedHash = hashOperations.entries(savedTokenValue);
// 		if (loadedHash == null) {
// 			System.out.println("null");
// 		}
// 		final RefreshToken refreshToken = (RefreshToken)hashMapper.fromHash(loadedHash);
// 		hashOperations.delete(refreshToken.refreshToken());
// 	}
// }
